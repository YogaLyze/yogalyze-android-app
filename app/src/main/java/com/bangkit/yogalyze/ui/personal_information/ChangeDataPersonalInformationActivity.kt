package com.bangkit.yogalyze.ui.personal_information

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.ActivityChangeDataPersonalInformationBinding
import com.google.firebase.auth.FirebaseAuth

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class ChangeDataPersonalInformationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityChangeDataPersonalInformationBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var Age: Int? = 0
    private var Gender: String? = null
    private var Height: Double? = 0.0
    private var Weight: Double? = 0.0
    private val personalInformationViewModel by viewModels<PersonalInformationViewModel> {
        PersonalInformationViewModel.personalInformationViewModelFactory(UserPreference.getInstance(dataStore))
    }


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
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
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

    override fun onClick(v: View) {
        when(v.id) {
            R.id.saveData -> {
                Age = binding.age.text.toString().toInt()
                Gender = binding.gender.text.toString()
                Height = binding.height.text.toString().toDouble()
                Weight = binding.height.text.toString().toDouble()

                personalInformationViewModel.getToken().observe(this){
                    personalInformationViewModel.saveData(it.toString(), Age, Gender, Height, Weight)
                }
            }
            R.id.back -> {
                val intent = Intent(this, PersonalInformationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}