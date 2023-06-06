package com.bangkit.yogalyze.ui.reset_password

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityResetPasswordSuccessBinding
import com.bangkit.yogalyze.ui.login.LoginActivity
import com.bangkit.yogalyze.ui.yoga_detail.YogaDetailActivity

class ResetPasswordSuccessActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityResetPasswordSuccessBinding
    private val resetPasswordViewModel by viewModels<ResetPasswordViewModel>()
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        email = intent.getStringExtra(EMAIL).toString()
        binding.email.text = email

        binding.resendButton.setOnClickListener(this)
        binding.loginButton.setOnClickListener(this)

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

    companion object{
        const val EMAIL = "email"
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.resendButton -> {
                resetPasswordViewModel.resetPassword(email)
            }
            R.id.loginButton -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}