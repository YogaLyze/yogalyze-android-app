package com.bangkit.yogalyze.ui.score

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityScoreBinding
import com.bangkit.yogalyze.ui.yoga_detail.YogaDetailActivity


class ScoreActivity : AppCompatActivity() {

    private lateinit var binding : ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scoreTextView.text = intent.getStringExtra(SCORE)

        setupView()
        setupViewModel()

        binding.button.setOnClickListener(){
            super.onBackPressed()
        }
    }

    private fun setupViewModel() {

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
        const val SCORE = "score"
    }
}