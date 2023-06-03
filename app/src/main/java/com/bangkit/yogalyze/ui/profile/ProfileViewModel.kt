package com.bangkit.yogalyze.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.ui.personal_information.PersonalInformationViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    class ProfileViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(pref) as T
        }
    }
}