package com.bangkit.yogalyze.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.response.GetUserResponse
import com.bangkit.yogalyze.api.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel (private val pref: UserPreference): ViewModel() {
    private val _userData = MutableLiveData<GetUserResponse>()
    val userData: LiveData<GetUserResponse> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(token)
        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userData.value = response.body()

                    Log.d(TAG, response.body().toString())
                } else{
                    _isLoading.value = false
                    Log.d(TAG, response.body().toString())
                }
            }
            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getToken(): LiveData<LoginResponse> {
        return pref.getToken().asLiveData()
    }

    class homeViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(pref) as T
        }
    }
    companion object{
        private const val TAG = "HomeViewModel"
    }
}