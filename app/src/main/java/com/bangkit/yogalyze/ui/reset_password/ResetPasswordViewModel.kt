package com.bangkit.yogalyze.ui.reset_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yogalyze.Event
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel: ViewModel() {

    var firebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun resetPassword(email: String) {
        _isLoading.value = true
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoading.value = false
                    _isSuccess.value = true
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _isSuccess.value = false
                _statusMessage.value = Event("Password reset failed! Make sure you entered the correct email")
                }
            }
    }