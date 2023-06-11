package com.bangkit.yogalyze.api.response

import com.google.gson.annotations.SerializedName

data class CreateUserProfileResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("updated_profile")
	val updatedProfile: List<Int?>? = null
)
