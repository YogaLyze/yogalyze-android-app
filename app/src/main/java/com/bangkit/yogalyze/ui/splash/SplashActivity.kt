package com.bangkit.yogalyze.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.yogalyze.MainActivity
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class SplashActivity : AppCompatActivity() {
    private val splashScreenViewModel by viewModels<SplashScreenViewModel> {
        SplashScreenViewModel.SplashScreenViewModelFactory(UserPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            splashScreenViewModel.getToken().observe(this) {
                if (it != null) {
                    if (it.isEmpty()) {
                        val intent = Intent(this, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }, 3000)
    }
}