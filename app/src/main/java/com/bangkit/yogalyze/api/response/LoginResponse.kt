package com.bangkit.yogalyze.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("accessToken")
	var accessToken: String? = null
)
