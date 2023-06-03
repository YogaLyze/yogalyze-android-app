package com.bangkit.yogalyze.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Yoga (
    var name : String,
    var description : String,
    var duration : Int,
    var image : Int,
    var pose : ArrayList<Pose>
) : Parcelable



