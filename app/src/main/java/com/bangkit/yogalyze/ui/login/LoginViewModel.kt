package com.bangkit.yogalyze.ui.login

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.*
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.LoginRequest
import com.bangkit.yogalyze.api.RegisterRequest
import com.bangkit.yogalyze.api.response.LoginResponse
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val pref: UserPreference) : ViewModel(){

    var firebaseAuth = FirebaseAuth.getInstance()

    private val _userToken = MutableLiveData<String>()
    val userToken: LiveData<String> = _userToken

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun login(email: String, password: String) {
//        _isLoading.value = true
//        val request = LoginRequest(email, password) // Create RegisterRequest object
//        val client = ApiConfig.getApiService().login(request) // Pass RegisterRequest object
//        client.enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(
//                call: Call<LoginResponse>,
//                response: Response<LoginResponse>
//            ) {
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    _userToken.value= response.body()
//                    _statusMessage.value = Event("Login successful")
//                    viewModelScope.launch {
//                        pref.login(userToken.value?.accessToken.toString())
//                    }
//                } else {
//                    _isLoading.value = false
//                    _statusMessage.value = Event("Login failed! Please ensure the email and password are valid.")
//                    Log.d(TAG,response.toString())
//                }
//            }
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
        _isLoading.value = true
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = task.result.user
                    if (user!!.isEmailVerified){
                        _isLoading.value = false
                        _userToken.value = user.getIdToken(true).toString()
                        _statusMessage.value = Event("Login successful")
                        viewModelScope.launch {
                            pref.login(userToken.toString())
                        }
                    } else {
                        _isLoading.value = false
                        _statusMessage.value = Event("Login failed! Please ensure the email has been verified")
                    }
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _statusMessage.value = Event("Login failed! Please ensure the email and password are valid.")
            }
    }
    fun forgetPassword(email: String) {
        _isLoading.value = true
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoading.value = false
                    _statusMessage.value = Event("Reset Password has been sent to your email")
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        _isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                _isLoading.value = false
                _statusMessage.value = Event("Login successful")
                _userToken.value = idToken
                viewModelScope.launch {
                    pref.login(userToken.toString())
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    class LoginViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(pref) as T
        }
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }
}