package com.bangkit.yogalyze.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.LoginRequest
import com.bangkit.yogalyze.api.RegisterRequest
import com.bangkit.yogalyze.api.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val pref: UserPreference) : ViewModel(){

    private val _userToken = MutableLiveData<LoginResponse>()
    val userToken: LiveData<LoginResponse> = _userToken

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun login(email: String, password: String) {
        _isLoading.value = true
        val request = LoginRequest(email, password) // Create RegisterRequest object
        val client = ApiConfig.getApiService().login(request) // Pass RegisterRequest object
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userToken.value= response.body()
                    _statusMessage.value = Event("Login successful")
                    viewModelScope.launch {
                        pref.login(userToken.value?.accessToken.toString())
                    }
                } else {
                    _isLoading.value = false
                    _statusMessage.value = Event("Login failed! Please ensure the email and password are valid.")
                    Log.d(TAG,response.toString())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
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