package com.bangkit.yogalyze.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yogalyze.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest

class RegisterViewModel : ViewModel() {

    var firebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>> = _statusMessage

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = name
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            user.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        _isLoading.value = false
                                        _isRegistered.value = true
                                        _statusMessage.value = Event("Registration successful! Please verify your email before login")
                                    }
                                }
                        }
                } else {
                    _isLoading.value = false
                    _isRegistered.value = false
                    _statusMessage.value = Event("Registration failed")

                }
            }
            .addOnFailureListener { error ->
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${error.message}")
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        _isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                _isLoading.value = false
                _isRegistered.value = true
                _statusMessage.value = Event("Registration Successful! Please login")
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}