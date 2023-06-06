package com.bangkit.yogalyze.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.ui.personal_information.PersonalInformationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {
    var firebaseAuth = FirebaseAuth.getInstance()

    lateinit var googleSignInClient: GoogleSignInClient

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("587913373658-4cmjckncas3clec372iv2bmj3mej499j.apps.googleusercontent.com")
        .requestEmail()
        .build();


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