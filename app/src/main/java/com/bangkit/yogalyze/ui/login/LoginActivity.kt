package com.bangkit.yogalyze.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import com.bangkit.yogalyze.MainActivity
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.ActivityLoginBinding
import com.bangkit.yogalyze.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModel.LoginViewModelFactory(UserPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        binding.loginButton.setOnClickListener(this)
        binding.loginWithGoogleButton.setOnClickListener(this)
        binding.loginWithFacebookButton.setOnClickListener(this)
        binding.forgetPasswordTextView.setOnClickListener(this)
        binding.registerTextView.setOnClickListener(this)

    }

    private fun setupViewModel() {
        loginViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        loginViewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled().let {
                if(it != null) {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        })

        loginViewModel.userToken.observe(this){
            if (it != null){
                val intent = Intent(this, MainActivity::class.java)
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
        when(v.id) {
            R.id.loginButton -> {
                email = binding.emailEditText.text.toString()
                password = binding.passwordEditText.text.toString()

                when {
                    email.isEmpty() -> {
                        binding.emailEditTextLayout.error = "Enter email"
                    }
                    password.isEmpty() -> {
                        binding.passwordEditTextLayout.error = "Enter Password"
                    }
                    else -> {
                        loginViewModel.login(email, password)
                    }
                }
            }
            R.id.registerTextView -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}