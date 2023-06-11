package com.bangkit.yogalyze.api.response

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("user_history")
	val userHistory: List<UserHistoryItem?>? = null
)

data class UserHistoryItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("yoga_pose")
	val yogaPose: String? = null,

	@field:SerializedName("yoga_type")
	val yogaType: String? = null
)
