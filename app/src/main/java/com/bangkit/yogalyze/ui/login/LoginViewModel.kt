package com.bangkit.yogalyze.ui.login

import androidx.lifecycle.*
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.UserPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginViewModel (private val pref: UserPreference) : ViewModel(){

    var firebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun login(email: String, password: String) {
        _isLoading.value = true
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = task.result.user
                    if (user!!.isEmailVerified){
                        _isLoading.value = false
                        _statusMessage.value = Event("Login successful")
                        _isLogin.value = true
                    } else {
                        _isLoading.value = false
                        _statusMessage.value = Event("Login failed! Please ensure the email has been verified")
                    }
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _statusMessage.value = Event("Login failed! Please ensure the email and password are valid.")
            }
    }
    fun forgetPassword(email: String) {
        _isLoading.value = true
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoading.value = false
                    _statusMessage.value = Event("Reset Password has been sent to your email")
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        _isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                _isLoading.value = false
                _statusMessage.value = Event("Login successful")
                _isLogin.value = true
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    fun saveToken(token : String){
        viewModelScope.launch {
            pref.login(token)
        }
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