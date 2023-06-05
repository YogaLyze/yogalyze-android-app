//package com.bangkit.yogalyze.ui.camera
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Build
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.TextureView
//import android.view.WindowInsets
//import android.view.WindowManager
//import android.widget.Toast
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProxy
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.bangkit.yogalyze.R
//import com.bangkit.yogalyze.databinding.ActivityCameraBinding
//import com.bangkit.yogalyze.ui.yoga_detail.YogaDetailActivity
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//class CameraActivity : AppCompatActivity() {
//
//    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//    private lateinit var binding : ActivityCameraBinding
//    private lateinit var cameraExecutor: ExecutorService
//    private var imageCapture : ImageCapture? = null
//    private var imageAnalyzer: ImageAnalysis? = null
//    private lateinit var poseName : String
//    private lateinit var gmbr : ImageProxy
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (!allPermissionsGranted()) {
//                Toast.makeText(
//                    this,
//                    "Not getting permission",
//                    Toast.LENGTH_SHORT
//                ).show()
//                finish()
//            }
//        }
//    }
//
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCameraBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        setupView()
//
//        if (!allPermissionsGranted()) {
//            ActivityCompat.requestPermissions(
//                this,
//                REQUIRED_PERMISSIONS,
//                REQUEST_CODE_PERMISSIONS
//            )
//        }
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        binding.poseImageView.setImageResource(intent.getIntExtra(EXTRA_IMAGE, 0))
//        poseName = intent.getStringExtra(EXTRA_POSE).toString()
//        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//
//        val textureView: TextureView = findViewById(R.id.textureView)
//        preview.setSurfaceProvider(textureView.createSurfaceProvider())
//
//        startCamera()
//
//    }
//
//    private fun startCamera() {
//
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
////            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
////            val preview = Preview.Builder()
////                .build()
////                .also {
////                    it.setSurfaceProvider(binding.camera.surfaceProvider)
////                }
////
////            imageCapture = ImageCapture.Builder()
////                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
////                .build()
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//            cameraProviderFuture.addListener({
//                val cameraProvider = cameraProviderFuture.get()
//                bindCameraUseCases(cameraProvider)
//            }, ContextCompat.getMainExecutor(this))
//
//
//
//
////            try {
////                cameraProvider.unbindAll()
////                cameraProvider.bindToLifecycle(
////                    this,
////                    cameraSelector,
////                    preview,
////                    imageCapture,
////                    imageAnalyzer
////                )
////            } catch (exc: Exception) {
////                Toast.makeText(
////                    this@CameraActivity,
////                    "Gagal memunculkan kamera.",
////                    Toast.LENGTH_SHORT
////                ).show()
////            }
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    private fun setupView() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.hide()
//    }
//
//    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {
//        val preview = Preview.Builder().build()
//        val imageAnalysis = ImageAnalysis.Builder()
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//
//        imageAnalysis.setAnalyzer(cameraExecutor, poseDetectorAnalyzer)
//
//        val cameraSelector = CameraSelector.Builder()
//            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//            .build()
//
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
//    }
//
//    val poseDetectorAnalyzer = ImageAnalysis.Analyzer { image ->
//        // Proses frame gambar dan deteksi pose
//        // ...
//        image.close()
//    }
//
//
//
//    companion object {
//        const val CAMERA_X_RESULT = 200
//        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//        private const val REQUEST_CODE_PERMISSIONS = 10
//        const val EXTRA_IMAGE = "extra_image"
//        const val EXTRA_YOGA = "extra_yoga"
//        const val EXTRA_POSE = "extra_pose"
//    }
//}

package com.bangkit.yogalyze.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.TextureView
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var poseName: String
    private lateinit var yogaName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.poseImageView.setImageResource(intent.getIntExtra(EXTRA_IMAGE, 0))
        poseName = intent.getStringExtra(EXTRA_POSE).toString()
        yogaName = intent.getStringExtra(EXTRA_YOGA).toString()

        val textureView: TextureView = findViewById(R.id.textureView)
        val previewView: PreviewView = findViewById(R.id.camera)
        textureView.post { startCamera(previewView) }
    }

    private fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases(cameraProvider, previewView)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases(
        cameraProvider: ProcessCameraProvider,
        textureView: PreviewView
    ) {
        val preview = Preview.Builder()
            .setTargetResolution(Size(textureView.width, textureView.height))
            .build()

        preview.setSurfaceProvider(textureView.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(textureView.width, textureView.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val poseDetectorAnalyzer = ImageAnalysis.Analyzer { image ->
            val poseDetected = true

            // Perbarui nilai timer jika pose terdeteksi
            if (poseDetected) {
                runOnUiThread {
                    binding.timer.text = "10" // Ubah teks menjadi "10"
                }
            }

            image.close()
        }

        imageAnalysis.setAnalyzer(cameraExecutor, poseDetectorAnalyzer)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            Toast.makeText(
                this@CameraActivity,
                "Failed to start camera.",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

        override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Not getting permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_YOGA = "extra_yoga"
        const val EXTRA_POSE = "extra_pose"
    }
}

