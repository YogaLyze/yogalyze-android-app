package com.bangkit.yogalyze.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.yogalyze.UserPreference
import kotlinx.coroutines.launch

class AlarmViewModel (private val pref: UserPreference): ViewModel(){
    fun saveTime(time : String){
        viewModelScope.launch {
            pref.saveTime(time)
        }
    }

    fun getTime(): LiveData<String> {
        return pref.getTime().asLiveData()
    }

    fun cancelAlarm(){
        viewModelScope.launch {
            pref.deleteTime()
        }
    }

    class AlarmViewModelFactory(private val pref : UserPreference) :
        ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlarmViewModel(pref) as T
        }
    }
}