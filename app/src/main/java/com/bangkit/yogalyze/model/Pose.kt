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

val downward_facing_dog_pose = Pose("Adho Mukha Svanasana", "Downward Facing Dog Pose", 24, R.drawable.downward_facing_dog_pose)
val cobra_pose = Pose("Bhujangasana", "Cobra Pose", 24, R.drawable.cobra_pose)
val bridge_pose = Pose("Setu Bandhasana", "Bridge Pose", 24, R.drawable.bridge_pose)
val child_pose = Pose("Balasana", "Child Pose", 24, R.drawable.child_pose)
val locust_pose = Pose("Salabhasana","Locust Pose", 24, R.drawable.locust_pose)
val tree_pose = Pose("Vrikshasana", "Tree Pose", 24, R.drawable.tree_pose)
val triangle_pose = Pose("Trikonasana", "Triangle Pose", 24, R.drawable.triangle_pose)
val standing_forward_bend = Pose("Uttanasana", "Standing Forward Bend", 24, R.drawable.standing_forward_bend_pose)
val fish_pose = Pose("Matsyasana", "Fish Pose", 24, R.drawable.fish_pose)
val plow_pose = Pose("Halasana", "Plow Pose", 24, R.drawable.plow_pose)
val catcow_pose = Pose("Bitilasana Marjaryasana", "Cat-Cow Pose", 24, R.drawable.catcow_pose)
val bow_pose = Pose("Dhanurasana", "Bow Pose", 24, R.drawable.bow_pose)
val low_lunge_pose = Pose("Anjneyasana", "Low Lunge Pose", 24, R.drawable.low_lunge_pose)
val cow_face_pose = Pose("Gomukhasana", "Cow Face Pose", 24, R.drawable.cow_face_pose)
val warrior_ii_pose = Pose("Virabhadrasana II", "Warrior II Pose", 24, R.drawable.warrior_ii_pose)
val corpse_pose = Pose("Savana", "Corpse Pose", 24, R.drawable.corpse_pose)

var backpain = arrayListOf(downward_facing_dog_pose, cobra_pose, bridge_pose, child_pose, locust_pose)
var anxiety = arrayListOf(tree_pose, triangle_pose, standing_forward_bend, fish_pose, child_pose)
var flexibility = arrayListOf(plow_pose, catcow_pose, bow_pose, low_lunge_pose, cow_face_pose)
var neck_pain = arrayListOf(standing_forward_bend, warrior_ii_pose, triangle_pose, cow_face_pose, corpse_pose)
