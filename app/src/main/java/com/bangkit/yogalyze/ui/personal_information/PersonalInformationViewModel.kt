package com.bangkit.yogalyze.ui.personal_information

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.UpdateUserDataRequest
import com.bangkit.yogalyze.api.response.CreateUserProfileResponse
import com.bangkit.yogalyze.api.response.GetUserProfileResponse
import com.bangkit.yogalyze.api.response.Profile
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response

class PersonalInformationViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _birthDateData = MutableLiveData<String>()
    val birthDateData: LiveData<String> = _birthDateData

    private val _genderData = MutableLiveData<String>()
    val genderData: LiveData<String> = _genderData

    private val _weightData = MutableLiveData<String>()
    val weightData: LiveData<String> = _weightData

    private val _heightData = MutableLiveData<String>()
    val heightData: LiveData<String> = _heightData

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun saveData(token: String, birthDate: String?, gender: String?, height: Double?, weigth: Double?){
        _isLoading.value = true
        var data = UpdateUserDataRequest(birthDate, gender, height, weigth)
        Log.d(TAG, data.toString())
        val client = ApiConfig.getApiService().createUserProfile(token, data)
        client.enqueue(object : retrofit2.Callback<CreateUserProfileResponse> {
            override fun onResponse(
                call: Call<CreateUserProfileResponse>,
                response: Response<CreateUserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _statusMessage.value = Event("Data saved successfully")
                    _isSaved.value = true

                } else {
                    _isLoading.value = false
                    _isSaved.value = false
                    _statusMessage.value = Event("Data failed to save")
                    Log.d(TAG, "failed ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CreateUserProfileResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun getData(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserProfile(token)
        client.enqueue(object : retrofit2.Callback<GetUserProfileResponse> {
            override fun onResponse(
                call: Call<GetUserProfileResponse>,
                response: Response<GetUserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    _birthDateData.value = responseBody?.profile?.dateOfBirth.toString()
                    _genderData.value = responseBody?.profile?.gender.toString()
                    _heightData.value = responseBody?.profile?.height.toString()
                    _weightData.value = responseBody?.profile?.weight.toString()
                    Log.e(TAG, responseBody?.profile.toString())
                } else {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t:Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun getToken() : LiveData<String> {
        var firebaseUser = FirebaseAuth.getInstance().currentUser
        var token = MutableLiveData<String>()


        firebaseUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = "Bearer " + task.result.token
                    token.value = idToken
                    Log.d(TAG, idToken)
                }
            }
            ?.addOnFailureListener { error ->
                Log.e(TAG, error.message.toString())
            }

        return token
    }

    companion object{
        const val TAG = "PersonalInformationViewModel"
    }
}