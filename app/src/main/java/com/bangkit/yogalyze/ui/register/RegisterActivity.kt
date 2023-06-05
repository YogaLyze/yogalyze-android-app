package com.bangkit.yogalyze.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityRegisterBinding
import com.bangkit.yogalyze.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmpass: String
    private val registerViewModel by viewModels<RegisterViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        binding.registerButton.setOnClickListener(this)
        binding.registerWithGoogleButton.setOnClickListener(this)
        binding.loginTextView.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("587913373658-4cmjckncas3clec372iv2bmj3mej499j.apps.googleusercontent.com")
            .requestEmail()
            .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupViewModel() {
        registerViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        registerViewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled().let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        registerViewModel.isRegistered.observe(this){
            if(it == true) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.registerButton -> {
                name = binding.nameEditText.text.toString()
                email = binding.emailEditText.text.toString()
                password = binding.passwordEditText.text.toString()
                confirmpass = binding.confirmPasswordEditText.text.toString()

                when {
                    name.isEmpty() -> {
                        binding.nameEditTextLayout.error = "Enter name"
                    }

                    email.isEmpty() -> {
                        binding.emailEditTextLayout.error = "Enter email"
                    }

                    password.isEmpty() -> {
                        binding.passwordEditTextLayout.error = "Enter Password"
                    }

                    confirmpass.isEmpty() -> {
                        binding.confirmPasswordEditTextLayout.error = "Enter Password"
                    }

                    password.length < 6 -> {
                        binding.passwordEditTextLayout.error = "Password at least 6 characters"
                    }

                    confirmpass != password -> {
                        binding.confirmPasswordEditTextLayout.error = "Passwords do not match"
                    }

                    else -> {
                        registerViewModel.register(name, email, password)
                    }
                }
            }
            R.id.registerWithGoogleButton -> {
                googleSignInClient.signOut()
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, 1001)
            }
            R.id.loginTextView -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                registerViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }
}