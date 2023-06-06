package com.bangkit.yogalyze.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityCameraBinding
import com.bangkit.yogalyze.ml.BackPain
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var poseName: String
    private lateinit var yogaName: String
    lateinit var model1: BackPain
    lateinit var labels: List<String>
    var outputFeature0: FloatArray? = null

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
        labels = FileUtil.loadLabels(this, "labelBackPain.txt")
        model1 = BackPain.newInstance(this)
        val textureView: TextureView = findViewById(R.id.textureView)
        val previewView: PreviewView = findViewById(R.id.camera)
        textureView.post { startCamera(previewView,textureView) }
    }

    private fun startCamera(previewView: PreviewView, textureView: TextureView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases(cameraProvider, previewView, textureView)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases(
        cameraProvider: ProcessCameraProvider,
        textureView: PreviewView,
        tv: TextureView
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
            var poseDetected = true

            // Perbarui nilai timer jika pose terdeteksi
            if (poseDetected) {
                runOnUiThread {
                    val bitmap = image.toBitmap()
                    val gambar = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
                    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 100, 100, 3), DataType.FLOAT32)
                    val tensorImage = TensorImage(DataType.FLOAT32)
                    tensorImage.load(gambar)
                    val byteBuffer = tensorImage.buffer
                    inputFeature0.loadBuffer(byteBuffer)
                    val outputs1 = model1.process(inputFeature0)
                    outputFeature0 = outputs1.outputFeature0AsTensorBuffer.floatArray
                    val threshold = 0.5f
                    val detectedScores = mutableListOf<Float>()

                    outputFeature0?.forEachIndexed { index, score ->
                        if (score > threshold) {
                            runOnUiThread {
                                binding.accuracyTextView.text = score.toString()
                            }

                        }
                    }

                    if(gambar!=null){
                        binding.timer.text = "10"
                        poseDetected=false
                    }
                     // Ubah teks menjadi "10"
                }
            }
            if(!poseDetected){
                image.close()
            }
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
