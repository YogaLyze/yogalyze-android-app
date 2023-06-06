package com.bangkit.yogalyze.ui.personal_information

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.UpdateUserDataRequest
import com.bangkit.yogalyze.api.response.GetUserResponse
import com.bangkit.yogalyze.api.response.LoginResponse
import com.bangkit.yogalyze.api.response.UpdateUserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class PersonalInformationViewModel (private var pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _userData = MutableLiveData<GetUserResponse>()
    val userData: LiveData<GetUserResponse> = _userData

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun saveData(token: String, age: Int?, gender: String?, height: Double?, weigth: Double?){
        _isLoading.value = true
        Log.d("cektoken", token)
        var data = UpdateUserDataRequest(age, gender, height, weigth)
        val client = ApiConfig.getApiService().updateUserData(token, data)
        client.enqueue(object : retrofit2.Callback<UpdateUserResponse> {
            override fun onResponse(
                call: Call<UpdateUserResponse>,
                response: Response<UpdateUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _statusMessage.value = Event("Data saved successfully")
                    _isSaved.value = true

                } else {
                    _isLoading.value = false
                    _isSaved.value = false
                    _statusMessage.value = Event("Data failed to save")
                    Log.d("uploadData", response.toString())
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("uploadDataFailure", "onFailure: ${t.message}")
            }
        })

    }

    fun getData(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(token)
        client.enqueue(object : retrofit2.Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userData.value = response.body()

                } else {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("uploadDataFailure", "onFailure: ${t.message}")
            }
        })

    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    class personalInformationViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PersonalInformationViewModel(pref) as T
        }
    }
}