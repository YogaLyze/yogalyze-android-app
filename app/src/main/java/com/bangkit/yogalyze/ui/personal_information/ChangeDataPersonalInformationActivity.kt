package com.bangkit.yogalyze.ui.personal_information

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.databinding.ActivityChangeDataPersonalInformationBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Suppress("DEPRECATION")
class ChangeDataPersonalInformationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityChangeDataPersonalInformationBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var BirthDate: String? = null
    private var Gender: String? = null
    private var Height: Double? = 0.0
    private var Weight: Double? = 0.0
    private val calendar = Calendar.getInstance()
    private lateinit var selectedDate : String
    private val personalInformationViewModel by viewModels<PersonalInformationViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeDataPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        binding.name.text = firebaseAuth.currentUser!!.displayName
        binding.email.text = firebaseAuth.currentUser!!.email

        binding.saveData.setOnClickListener(this)
        binding.back.setOnClickListener(this)
        binding.datePickerButton.setOnClickListener(this)
    }

    private fun setupViewModel() {
        personalInformationViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it.equals(true)) View.VISIBLE else View.GONE
        }

        personalInformationViewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled().let {
                if (it != null) {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        })

        personalInformationViewModel.isSaved.observe(this){
            if (it == true){
                val intent = Intent(this, PersonalInformationActivity::class.java)
                startActivity(intent)
                finish()
            }

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
        when(v.id) {
            R.id.saveData -> {
                BirthDate = binding.datePicker.text.toString()
                Height = binding.height.text.toString().toDoubleOrNull()
                Weight = binding.weight.text.toString().toDoubleOrNull()

                val selectedGenderId = binding.radioGroupGender.checkedRadioButtonId

                if (selectedGenderId != -1) {
                    val selectedGenderRadioButton = findViewById<RadioButton>(selectedGenderId)
                    Gender = selectedGenderRadioButton.text.toString()
                } else {
                    Gender = null
                }

                if (BirthDate.isNullOrEmpty() || Gender.isNullOrEmpty() || Height == null || Weight == null) {
                    Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
                } else {
                    personalInformationViewModel.getToken().observe(this) { token ->
                        personalInformationViewModel.saveData(
                            token.toString(),
                            BirthDate,
                            Gender,
                            Weight,
                            Height
                        )
                    }
                    Log.d("databirthday", BirthDate.toString())
                }
            }
            R.id.back -> {
                val intent = Intent(this, PersonalInformationActivity::class.java)
                startActivity(intent)
                finish()

            }
            R.id.datePickerButton -> {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time)

                binding.datePicker.text = selectedDate
            }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}