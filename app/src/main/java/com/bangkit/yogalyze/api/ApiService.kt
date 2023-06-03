package com.bangkit.yogalyze.api

import com.bangkit.yogalyze.api.response.GetUserResponse
import com.bangkit.yogalyze.api.response.LoginResponse
import com.bangkit.yogalyze.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/auth/register")
    fun register(
        @Body request : RegisterRequest
    ): Call<RegisterResponse>

    @POST("/auth/login")
    fun login(
        @Body request : LoginRequest
    ): Call<LoginResponse>

    @GET("/user/profile")
    fun getUser(
        @Header("Authorization") token : String
    ): Call<GetUserResponse>
}

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmpass: String
)

data class LoginRequest(
    val email: String,
    val password: String
)