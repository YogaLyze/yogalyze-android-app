package com.bangkit.yogalyze.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityWelcomeBinding
import com.bangkit.yogalyze.ui.login.LoginActivity
import com.bangkit.yogalyze.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()

        binding.loginButton.setOnClickListener(this)
        binding.registerButton.setOnClickListener(this)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.welcomeImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val button = ObjectAnimator.ofFloat(binding.buttonToLoginRegister, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.appName, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.appDesc, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, desc, button)
            start()
        }
    }

    private fun setupView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

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
        when(v.id){
            R.id.loginButton -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.registerButton -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}