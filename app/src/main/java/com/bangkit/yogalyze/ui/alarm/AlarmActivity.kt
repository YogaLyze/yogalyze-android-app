package com.bangkit.yogalyze.ui.alarm

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.UserPreference
import com.bangkit.yogalyze.databinding.ActivityAlarmBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class AlarmActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener, View.OnClickListener {

    private var binding : ActivityAlarmBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver
    private val alarmViewModel by viewModels<AlarmViewModel> (){
        AlarmViewModel.AlarmViewModelFactory(UserPreference.getInstance(dataStore))
    }

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupView()

        binding?.btnRepeatingTime?.setOnClickListener(this)
        binding?.btnSetRepeatingAlarm?.setOnClickListener(this)
        binding?.btnCancelRepeatingAlarm?.setOnClickListener(this)
        binding?.closeButton?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()

        alarmViewModel.getTime().observe(this) { token ->
            if (token != "") {
                binding?.tvRepeatingTime?.text = token
            } else {
                binding?.tvRepeatingTime?.text = "Select the desired time"
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_repeating_time -> {
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_set_repeating_alarm -> {
                val repeatTime = binding?.tvRepeatingTime?.text.toString()
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                    repeatTime)
            }
            R.id.btn_cancel_repeating_alarm -> {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
                alarmViewModel.cancelAlarm()
                binding?.tvRepeatingTime?.text = "Select the desired time"
            }
            R.id.closeButton -> {
                onBackPressed()
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

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_REPEAT_TAG -> {
                val date = dateFormat.format(calendar.time)
                alarmViewModel.saveTime(date.toString())
                alarmViewModel.getTime().observe(this){
                    binding?.tvRepeatingTime?.text = it
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}