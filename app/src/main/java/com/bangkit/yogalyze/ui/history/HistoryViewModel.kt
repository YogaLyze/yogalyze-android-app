package com.bangkit.yogalyze.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.yogalyze.Event
import com.bangkit.yogalyze.api.ApiConfig
import com.bangkit.yogalyze.api.HistoryDataRequest
import com.bangkit.yogalyze.api.response.AddHistoryResponse
import com.bangkit.yogalyze.api.response.GetHistoryResponse
import com.bangkit.yogalyze.api.response.UserHistoryItem
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _historyData = MutableLiveData<List<UserHistoryItem>>()
    val historyData: LiveData<List<UserHistoryItem>> = _historyData

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage : LiveData<Event<String>> = _statusMessage

    fun saveHistory(token: String, yogaType : String, yogaPose: String, score: Int? = 0, date: String){
        Log.d("dataHistoryy", "${yogaType} ${yogaPose} ${score} ${date}")
        _isLoading.value = true
        val data = HistoryDataRequest(date, score, yogaPose, yogaType)
        val client = ApiConfig.getApiService().addHistory(token, data)
        client.enqueue(object : retrofit2.Callback<AddHistoryResponse> {
            override fun onResponse(
                call: Call<AddHistoryResponse>,
                response: Response<AddHistoryResponse>
            ) {
                _isLoading.value = false
            }

            override fun onFailure(call: Call<AddHistoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("statusHistory", "onFailure: ${t.message}")
            }
        })
    }

    fun getHistory(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getHistory(token)
        client.enqueue(object : retrofit2.Callback<GetHistoryResponse> {
            override fun onResponse(
                call: Call<GetHistoryResponse>,
                response: Response<GetHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val a = response.body()!!.userHistory.toString()
                    if(a != "[]"){
                        _historyData.value = response.body()?.userHistory as List<UserHistoryItem>
                    }

                } else {
                    _isLoading.value = false
                    Log.d(TAG, "failed + ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun getToken() : LiveData<String> {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val token = MutableLiveData<String>()


        firebaseUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = "Bearer " + task.result.token
                    token.value = idToken
                    Log.d("TokenUser", idToken)
                }
            }
            .addOnFailureListener { error ->
                Log.e("LoginActivity", error.message.toString())
            }

        return token
    }

    companion object{
        const val TAG = "HistoryViewModel"
    }
}