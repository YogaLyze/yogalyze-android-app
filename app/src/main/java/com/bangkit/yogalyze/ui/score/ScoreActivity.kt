package com.bangkit.yogalyze.ui.score

import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.yogalyze.databinding.ActivityScoreBinding
@Suppress("DEPRECATION")
class ScoreActivity : AppCompatActivity() {

    private lateinit var binding : ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scoreTextView.text = intent.getStringExtra(SCORE)
        val byteArray = intent.getByteArrayExtra(PHOTO)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        binding.ivUserPhoto.setImageBitmap(bitmap)

        setupView()

        binding.button.setOnClickListener(){
            super.onBackPressed()
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



    companion object{
        const val SCORE = "score"
        const val PHOTO = "photo"
    }
}