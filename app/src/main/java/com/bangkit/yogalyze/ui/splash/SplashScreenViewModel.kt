package com.bangkit.yogalyze.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bangkit.yogalyze.UserPreference


class SplashScreenViewModel (private var pref: UserPreference) : ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    class SplashScreenViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashScreenViewModel(pref) as T
        }
    }
}