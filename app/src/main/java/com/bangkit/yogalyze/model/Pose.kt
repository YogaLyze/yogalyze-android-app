package com.bangkit.yogalyze.model

import android.os.Parcelable
import com.bangkit.yogalyze.R
import kotlinx.parcelize.Parcelize


@Parcelize
data class Pose (
    var shanskrit : String,
    var name : String,
    var duration : Int,
    var image: Int
) : Parcelable
