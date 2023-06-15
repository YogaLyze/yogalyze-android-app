package com.bangkit.yogalyze.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.yogalyze.UserPreference
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {
    var firebaseAuth = FirebaseAuth.getInstance()

    fun logout() {
        firebaseAuth.signOut()
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun delete() {
        firebaseAuth.currentUser!!.delete()
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