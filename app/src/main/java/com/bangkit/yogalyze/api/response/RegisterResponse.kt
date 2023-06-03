package com.bangkit.yogalyze.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("msg")
	var msg: String? = null
)
