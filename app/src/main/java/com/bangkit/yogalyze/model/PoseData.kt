package com.bangkit.yogalyze.model

import com.bangkit.yogalyze.R
import java.util.ArrayList

object PoseData {
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
    val side_stretch_pose = Pose("Parsvottanasana", "Side Stretch Pose", 24, R.drawable.side_stretch_pose)
    val bow_pose = Pose("Dhanurasana", "Bow Pose", 24, R.drawable.bow_pose)
    val low_lunge_pose = Pose("Anjneyasana", "Low Lunge Pose", 24, R.drawable.low_lunge_pose)
    val cow_face_pose = Pose("Gomukhasana", "Cow Face Pose", 24, R.drawable.cow_face_pose)
    val warrior_ii_pose = Pose("Virabhadrasana II", "Warrior II Pose", 24, R.drawable.warrior_ii_pose)
    val corpse_pose = Pose("Savana", "Corpse Pose", 24, R.drawable.corpse_pose)

    val backpain: ArrayList<Pose>
        get() {
            val list = arrayListOf<Pose>()
            list.add(downward_facing_dog_pose)
            list.add(cobra_pose)
            list.add(bridge_pose)
            list.add(child_pose)
            list.add(locust_pose)
            return list
        }

    val anxiety: ArrayList<Pose>
        get() {
            val list = arrayListOf<Pose>()
            list.add(tree_pose)
            list.add(triangle_pose)
            list.add(standing_forward_bend)
            list.add(fish_pose)
            list.add(child_pose)
            return list
        }

    val flexibility: ArrayList<Pose>
        get() {
            val list = arrayListOf<Pose>()
            list.add(plow_pose)
            list.add(side_stretch_pose)
            list.add(bow_pose)
            list.add(low_lunge_pose)
            list.add(cow_face_pose)
            return list
        }

    val neckPain: ArrayList<Pose>
        get() {
            val list = arrayListOf<Pose>()
            list.add(standing_forward_bend)
            list.add(warrior_ii_pose)
            list.add(triangle_pose)
            list.add(cow_face_pose)
            list.add(corpse_pose)
            return list
        }

}