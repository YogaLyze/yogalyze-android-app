package com.bangkit.yogalyze.ui.yogalyze_video

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.yogalyze.MainActivity
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityYogalyzeVideoBinding
import com.bangkit.yogalyze.ui.home.HomeFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class YogalyzeVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYogalyzeVideoBinding
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYogalyzeVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupVideoView()

        binding.closeButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            player.release()
        }
    }


    private fun setupVideoView() {
        val videoPath = "android.resource://" + packageName + "/" + R.raw.yogalyze_video
        val videoUri = Uri.parse(videoPath)

        val dataSourceFactory = DefaultDataSourceFactory(this, "user-agent")
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = DefaultMediaSourceFactory(dataSourceFactory)
            .createMediaSource(mediaItem)
        val loopingSource = LoopingMediaSource(mediaSource)

        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player

        player.setMediaSource(loopingSource)
        player.prepare()
        player.play()

        player.repeatMode = Player.REPEAT_MODE_ONE
        binding.videoView.useController = false
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

    override fun onStart() {
        super.onStart()
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}