package com.bangkit.yogalyze.ui.reset_password

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangkit.yogalyze.MainActivity
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.ActivityResetPasswordBinding
import com.bangkit.yogalyze.ui.login.LoginActivity
import com.bangkit.yogalyze.ui.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityResetPasswordBinding
    private lateinit var email : String
    private val resetPasswordViewModel by viewModels<ResetPasswordViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        email = binding.emailEditText.text.toString()

        binding.backButton.setOnClickListener(this)
        binding.resetButton.setOnClickListener(this)
    }

    private fun setupViewModel() {
        resetPasswordViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        resetPasswordViewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled().let {
                if (it != null) {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        })

        resetPasswordViewModel.isSuccess.observe(this){
            if(it == true){
                val intent = Intent(this, ResetPasswordSuccessActivity::class.java)
                intent.putExtra(ResetPasswordSuccessActivity.EMAIL, email)
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
        when (v.id){
            R.id.resetButton -> {
                email = binding.emailEditText.text.toString()

                when {
                    email.isEmpty() -> {
                        binding.emailEditTextLayout.error = "Enter email"
                        Log.d("lihatEmail", email)
                    }

                    else -> {
                        resetPasswordViewModel.resetPassword(email)
                    }
                }
            }
            R.id.backButton -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}