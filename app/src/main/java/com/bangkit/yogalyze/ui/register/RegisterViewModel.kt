package com.bangkit.yogalyze.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.RegisterRequest
import com.bangkit.yogalyze.api.response.RegisterResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    var firebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerMsg = MutableLiveData<RegisterResponse>()
    val registerMsg: LiveData<RegisterResponse> = _registerMsg

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    fun register(name: String, email: String, password: String, confirmpass: String) {
//        _isLoading.value = true
//        val request = RegisterRequest(name, email, password, confirmpass) // Create RegisterRequest object
//        val client = ApiConfig.getApiService().register(request) // Pass RegisterRequest object
//        client.enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(
//                call: Call<RegisterResponse>,
//                response: Response<RegisterResponse>
//            ) {
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    _registerMsg.value = response.body()
//                    _statusMessage.value = Event("Registration Successful")
//
//                    Log.d(TAG, registerMsg.value?.msg.toString())
//
//                } else {
//                    _isLoading.value = false
//                    _statusMessage.value = Event("Registration Failed")
//                    Log.e(TAG, response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
        _isLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = name
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        _isLoading.value = false
                                        _statusMessage.value = Event("Registration Successful")
                                    }
                                }
                        }
                } else {
                    _isLoading.value = false
                    _statusMessage.value = Event("Registration Failed")

                }
            }
            .addOnFailureListener { error ->
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${error.message}")
            }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}