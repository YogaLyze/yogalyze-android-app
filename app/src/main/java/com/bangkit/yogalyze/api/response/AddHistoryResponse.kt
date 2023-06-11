package com.bangkit.yogalyze.api.response

import com.google.gson.annotations.SerializedName

data class AddHistoryResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("created_history")
	val createdHistory: CreatedHistory? = null
)

data class CreatedHistory(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("yoga_pose")
	val yogaPose: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("yoga_type")
	val yogaType: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
