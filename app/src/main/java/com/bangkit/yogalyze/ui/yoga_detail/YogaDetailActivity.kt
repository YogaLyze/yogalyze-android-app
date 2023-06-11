package com.bangkit.yogalyze.ui.yoga_detail


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.yogalyze.MainActivity
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.adapter.PoseAdapter
import com.bangkit.yogalyze.adapter.YogaAdapter
import com.bangkit.yogalyze.databinding.ActivityYogaDetailBinding
import com.bangkit.yogalyze.model.Pose
import com.bangkit.yogalyze.model.PoseData
import com.bangkit.yogalyze.model.Yoga
import com.bangkit.yogalyze.model.YogaData
import com.bangkit.yogalyze.ui.camera.CameraActivity
import com.bangkit.yogalyze.ui.home.HomeFragment


class YogaDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityYogaDetailBinding
    private lateinit var YogaName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYogaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        YogaName = intent.getStringExtra(EXTRA_NAME).toString()

        binding.yogaName.text = intent.getStringExtra(EXTRA_NAME)
        binding.yogaDuration.text = intent.getIntExtra(EXTRA_DURATION, 0).toString()
        binding.yogaDescription.text = intent.getStringExtra(EXTRA_DESCRIPTION)
        binding.yogaImage.setImageResource(intent.getIntExtra(EXTRA_IMAGE, 0))

        showPoses(intent.getParcelableArrayListExtra<Pose>(EXTRA_POSES))

        Log.d("yogaPose", intent.getParcelableArrayListExtra<Pose>(EXTRA_POSES).toString())

        binding.backButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_FRAGMENT, MainActivity.FRAGMENT_HOME)
            startActivity(intent)
            finish()
        }
    }

    private fun showPoses(data: ArrayList<Pose>?) {
        binding.yogaPoses.layoutManager = LinearLayoutManager(this)
        binding.yogaPoses.setHasFixedSize(true)
        val poseAdapter = data?.let { PoseAdapter(it) }
        binding.yogaPoses.adapter = poseAdapter
        poseAdapter!!.setOnItemClickCallback(object : PoseAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Pose) {
                showSelectedPose(data)
            }
        })

    }

    private fun showSelectedPose(data: Pose) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(CameraActivity.EXTRA_IMAGE, data.image)
        intent.putExtra(CameraActivity.EXTRA_POSE, data.name)
        intent.putExtra(CameraActivity.EXTRA_YOGA, YogaName)
        intent.putExtra(CameraActivity.EXTRA_DURATION, data.duration)
        Log.d("LihatYoga", YogaName)
        startActivity(intent)
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

    override fun onBackPressed() {
        // Ganti fragment dengan HomeFragment ketika tombol back ditekan
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentFrame, HomeFragment())
            .commit()
        super.onBackPressed()
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DURATION = "extra_duration"
        const val EXTRA_DESCRIPTION= "extra_description"
        const val EXTRA_IMAGE= "extra_image"
        const val EXTRA_POSES = "extra_poses"
    }
}