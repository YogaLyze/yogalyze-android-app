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

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmpass: String
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        binding.registerButton.setOnClickListener(this)
        binding.registerWithFacebookButton.setOnClickListener(this)
        binding.registerWithGoogleButton.setOnClickListener(this)
        binding.loginTextView.setOnClickListener(this)
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

        registerViewModel.registerMsg.observe(this){
            if(it.msg  == "Register Success") {
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

                    confirmpass != password -> {
                        binding.confirmPasswordEditTextLayout.error = "Passwords do not match"
                    }

                    else -> {
                        registerViewModel.register(name, email, password, confirmpass)
                    }
                }
            }
            R.id.loginTextView -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}