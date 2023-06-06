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
import com.bangkit.yogalyze.MainActivity
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.ActivityLoginBinding
import com.bangkit.yogalyze.databinding.ActivityPersonalInformationBinding
import com.bangkit.yogalyze.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class PersonalInformationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityPersonalInformationBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val personalInformationViewModel by viewModels<PersonalInformationViewModel> {
        PersonalInformationViewModel.personalInformationViewModelFactory(UserPreference.getInstance(dataStore))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        binding.name.text = firebaseAuth.currentUser!!.displayName
        binding.email.text = firebaseAuth.currentUser!!.email

        binding.changeData.setOnClickListener(this)
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

        personalInformationViewModel.getToken().observe(this){
            personalInformationViewModel.getData(it)
            personalInformationViewModel.userData.observe(this){
                binding.age.text = it.age.toString()
                binding.weight.text = it.weight.toString()
                binding.height.text = it.height.toString()
                binding.gender.text = it.gender.toString()
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
            R.id.changeData -> {
                val intent = Intent(this, ChangeDataPersonalInformationActivity::class.java)
                startActivity(intent)
            }
            R.id.back -> {
                // Navigasi dari Activity ke Fragment menggunakan FragmentManager dan FragmentTransaction
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = ProfileFragment()

                // Menambahkan Fragment ke container di dalam Activity
                fragmentTransaction.add(R.id.container, fragment)
                fragmentTransaction.commit()
            }
        }
    }
}