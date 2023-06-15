package com.bangkit.yogalyze.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityCameraBinding
import com.bangkit.yogalyze.ml.Anxiety
import com.bangkit.yogalyze.ml.BackPain
import com.bangkit.yogalyze.ml.Flexibility
import com.bangkit.yogalyze.ml.NeckPain
import com.bangkit.yogalyze.ui.history.HistoryViewModel
import com.bangkit.yogalyze.ui.score.ScoreActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.util.Calendar

@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var poseName: String
    private lateinit var yogaName: String
    private var duration : Int = 0
    lateinit var model1: BackPain
    lateinit var model2: Anxiety
    lateinit var model3: Flexibility
    lateinit var model4: NeckPain
    lateinit var labels: List<String>
    var outputFeature0: FloatArray? = null
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var text: TextView
    lateinit var userImage : Bitmap
    var bundle: Bundle? = null
    private var isProcessing = false
    private var countDownTimer: CountDownTimer? = null
    private var index : Int? = 0
    private var finalScore: Int? = 0
    private var isPermissionGranted = false
    private var isCountdownRunning = false
    private val historyViewModel by viewModels<HistoryViewModel>()

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
            if (allPermissionsGranted()) {
                isPermissionGranted = true
                openCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permission not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

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
        } else {
            isPermissionGranted = true
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.poseImageView.setImageResource(intent.getIntExtra(EXTRA_IMAGE, 0))
        poseName = intent.getStringExtra(EXTRA_POSE).toString()
        yogaName = intent.getStringExtra(EXTRA_YOGA).toString()
        duration = intent.getIntExtra(EXTRA_DURATION, 0)
        binding.timer.text = (duration*60).toString()
        Log.d("lihatDuration", duration.toString())
        when(yogaName){
            "Backpain" -> {
                labels = FileUtil.loadLabels(this, "labelBackPain.txt")
            }
            "Anxiety" -> {
                labels = FileUtil.loadLabels(this, "labelAnxiety.txt")
            }
            "Flexibility" -> {
                labels = FileUtil.loadLabels(this, "labelFlexibility.txt")
            }
            "Neck Pain" -> {
                labels = FileUtil.loadLabels(this, "labelNeckPain.txt")
            }
        }
        model1 = BackPain.newInstance(this)
        model2 = Anxiety.newInstance(this)
        model3 = Flexibility.newInstance(this)
        model4 = NeckPain.newInstance(this)
        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        imageView = findViewById(R.id.imageView)
        text = findViewById(R.id.accuracyTextView)
        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                if (!isProcessing) {
                    isProcessing = true

                    bitmap = textureView.bitmap!!
                    val bitmap1 = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

                    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 100, 100, 3), DataType.FLOAT32)
                    val tensorImage = TensorImage(DataType.FLOAT32)
                    tensorImage.load(bitmap1)
                    val byteBuffer = tensorImage.buffer
                    inputFeature0.loadBuffer(byteBuffer)
                    when(yogaName){
                        "Backpain" -> {
                            val outputs1 = model1.process(inputFeature0)
                            outputFeature0 = outputs1.outputFeature0AsTensorBuffer.floatArray
                        }
                        "Anxiety" -> {
                            val outputs2 = model2.process(inputFeature0)
                            outputFeature0 = outputs2.outputFeature0AsTensorBuffer.floatArray
                        }
                        "Flexibility" -> {
                            val outputs3 = model3.process(inputFeature0)
                            outputFeature0 = outputs3.outputFeature0AsTensorBuffer.floatArray
                        }
                        "Neck Pain" -> {
                            val outputs4 = model4.process(inputFeature0)
                            outputFeature0 = outputs4.outputFeature0AsTensorBuffer.floatArray
                        }
                    }

                    val indeks = labels.indexOf(poseName)
                    val score = outputFeature0?.getOrNull(indeks)
                    score?.let {
                        val formattedScore = String.format("%.2f", it)
                        val scoreWithPercentage = "${(formattedScore.toFloat() * 100).toInt()}%"
                        text.text = scoreWithPercentage
                        finalScore = (formattedScore.toFloat() * 100).toInt()

                        if(finalScore!! >= 90 && index == 0){
                            binding.poseGuide.text = "Hold Your Pose"
                            binding.poseGuide.setTextColor(Color.GREEN)
                            startCountdownTimer()
                            index = 1
                        }
                    }

                    // Delay selama 1 detik sebelum memproses gambar berikutnya
                    handler.postDelayed({
                        isProcessing = false
                    }, 1000)
                }


                val aspectRatio = textureView.width.toFloat() / textureView.height.toFloat()
                val targetWidth: Int
                val targetHeight: Int

                // Sesuaikan ukuran gambar berdasarkan rasio aspek TextureView
                if (bitmap.width.toFloat() / bitmap.height.toFloat() > aspectRatio) {
                    targetWidth = bitmap.width
                    targetHeight = (bitmap.width / aspectRatio).toInt()
                } else {
                    targetWidth = (bitmap.height * aspectRatio).toInt()
                    targetHeight = bitmap.height
                }

                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
                imageView.setImageBitmap(scaledBitmap)
                userImage = scaledBitmap

            }

        }
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    @SuppressLint("MissingPermission")
    fun openCamera() {
        if (isPermissionGranted) {
            cameraManager.openCamera(cameraManager.cameraIdList[1], object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera

                    val surfaceTexture = textureView.surfaceTexture
                    val surface = Surface(surfaceTexture)

                    val captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    captureRequest.addTarget(surface)

                    cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                        override fun onConfigured(session: CameraCaptureSession) {
                            session.setRepeatingRequest(captureRequest.build(), null, null)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {

                        }
                    }, handler)
                }

                override fun onDisconnected(camera: CameraDevice) {

                }

                override fun onError(camera: CameraDevice, error: Int) {

                }

            }, handler)

        }
    }

    private fun startCountdownTimer() {
        if (!isCountdownRunning) { // Tambahkan pengecekan isCountdownRunning
            val totalTimeInMillis = duration * 60000 // Waktu total dalam milidetik (misalnya 60 detik)

            countDownTimer = object : CountDownTimer(totalTimeInMillis.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    // Update tampilan setiap detik
                    // Misalnya: textView.text = "Sisa waktu: $seconds detik"
                    binding.timer.text = seconds.toString()
                }

                override fun onFinish() {
                    saveHistory(yogaName, poseName, finalScore!!)
                    getScore()
                }
            }

            countDownTimer?.start()
            isCountdownRunning = true
        }
    }

    // Tambahkan metode stopCountdownTimer()
    private fun stopCountdownTimer() {
        countDownTimer?.cancel()
        isCountdownRunning = false
    }

    // Ubah implementasi metode onBackPressed()
    override fun onBackPressed() {
        if (isCountdownRunning) {
            stopCountdownTimer()
            Toast.makeText(this, "Countdown timer stopped", Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    private fun saveHistory(yoga_name : String, yoga_pose : String, final_score : Int? = 0) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1 // Perhatikan penambahan 1 karena indeks bulan dimulai dari 0
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val date = "$year-$month-$day"

        historyViewModel.getToken().observe(this){
            historyViewModel.saveHistory(it, yoga_name, yoga_pose, final_score, date)
            Log.d("dataHistory", "${yoga_name} ${yoga_pose} ${final_score} ${date}")
        }
    }

    private fun getScore() {
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra(ScoreActivity.SCORE, finalScore.toString())
        val stream = ByteArrayOutputStream()
        val image = resizeBitmap(userImage, 500)
        image!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        intent.putExtra(ScoreActivity.PHOTO, byteArray)
        startActivity(intent)
        finish()
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap? {
        var width = bitmap.width
        var height = bitmap.height

        if (width <= 0 || height <= 0) {
            return null
        }

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
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

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_YOGA = "extra_yoga"
        const val EXTRA_POSE = "extra_pose"
        const val EXTRA_DURATION = "extra_duration"
    }
}
