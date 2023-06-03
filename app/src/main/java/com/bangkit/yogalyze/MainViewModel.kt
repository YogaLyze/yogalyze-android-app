package com.bangkit.yogalyze

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bangkit.yogalyze.api.response.LoginResponse


class MainViewModel (private var pref: UserPreference) : ViewModel() {

    fun getToken(): LiveData<LoginResponse> {
        return pref.getToken().asLiveData()
    }

    class MainViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(pref) as T
        }
    }
}