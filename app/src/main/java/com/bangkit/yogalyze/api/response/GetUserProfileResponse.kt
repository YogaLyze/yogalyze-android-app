package com.bangkit.yogalyze.api.response

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("profile")
	val profile: Profile? = null
)

data class Profile(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("weight")
	val weight: Double? = null,

	@field:SerializedName("height")
	val height: Double? = null
)
