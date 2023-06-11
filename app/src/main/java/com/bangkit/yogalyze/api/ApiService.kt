package com.bangkit.yogalyze.api

import com.bangkit.yogalyze.api.response.AddHistoryResponse
import com.bangkit.yogalyze.api.response.CreateUserProfileResponse
import com.bangkit.yogalyze.api.response.GetHistoryResponse
import com.bangkit.yogalyze.api.response.GetUserProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/user/profile")
    fun getUserProfile(
        @Header("Authorization") token : String
    ): Call<GetUserProfileResponse>

    @PUT("/user/profile")
    fun createUserProfile(
        @Header("Authorization") token: String,
        @Body request : UpdateUserDataRequest
    ): Call<CreateUserProfileResponse>

    @POST("/history/add")
    fun addHistory(
        @Header("Authorization") token: String,
        @Body request : HistoryDataRequest
    ): Call<AddHistoryResponse>

    @GET("/history/user")
    fun getHistory(
        @Header("Authorization") token : String
    ): Call<GetHistoryResponse>
}

data class HistoryDataRequest(
    val date: String,
    val score: Int? = 0,
    val yoga_pose: String,
    val yoga_type: String
)

data class UpdateUserDataRequest(
    val date_of_birth: String? = null,
    val gender: String? = null,
    val weight: Double? = 0.0,
    val height: Double? = 0.0
)
