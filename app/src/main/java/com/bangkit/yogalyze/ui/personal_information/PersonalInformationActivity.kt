package com.bangkit.yogalyze.ui.personal_information

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityPersonalInformationBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
class PersonalInformationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityPersonalInformationBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val personalInformationViewModel by viewModels<PersonalInformationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()

        binding.name.text = firebaseAuth.currentUser!!.displayName
        binding.email.text = firebaseAuth.currentUser!!.email

        binding.changeData.setOnClickListener(this)
        binding.backToProfile.setOnClickListener(this)

    }

    private fun setupViewModel() {
        personalInformationViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        personalInformationViewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled().let { message ->
                if (message != null) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        personalInformationViewModel.getToken().observe(this){
            personalInformationViewModel.getData(it)
            personalInformationViewModel.birthDateData.observe(this){
                Log.d("datadata", it)
                if (it != "null"){

                    val age = getAgeFromDateOfBirth(it)

                    binding.age.text = age.toString()
                }
            }
            personalInformationViewModel.genderData.observe(this){
                if (it != "null"){
                    binding.gender.text = it
                }
            }
            personalInformationViewModel.heightData.observe(this){
                if (it != "null"){
                    binding.height.text = it
                }
            }
            personalInformationViewModel.weightData.observe(this){
                if (it != "null"){
                    binding.weight.text = it
                }
            }
        }
    }

    fun getAgeFromDateOfBirth(dateString: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateOfBirth = dateFormat.parse(dateString)

        val calendar = Calendar.getInstance()
        calendar.time = dateOfBirth!!

        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
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
        when(v.id) {
            R.id.changeData -> {
                val intent = Intent(this, ChangeDataPersonalInformationActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.backToProfile -> {
                super.onBackPressed()
            }
        }
    }
}