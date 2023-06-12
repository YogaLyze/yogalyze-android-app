package com.bangkit.yogalyze.model

import com.bangkit.yogalyze.R
import java.util.ArrayList

object YogaData {

    val total_duration_backpain_yoga = PoseData.downward_facing_dog_pose.duration + PoseData.cobra_pose.duration + PoseData.bridge_pose.duration + PoseData.child_pose.duration + PoseData.locust_pose.duration
    val total_duration_anxiety_yoga = PoseData.tree_pose.duration + PoseData.triangle_pose.duration + PoseData.standing_forward_bend.duration + PoseData.fish_pose.duration + PoseData.child_pose.duration
    val total_duration_flexibility_yoga = PoseData.plow_pose.duration + PoseData.side_stretch_pose.duration + PoseData.bow_pose.duration + PoseData.low_lunge_pose.duration + PoseData.cow_face_pose.duration
    val total_duration_neckpain_yoga = PoseData.standing_forward_bend.duration + PoseData.warrior_ii_pose.duration + PoseData.triangle_pose.duration + PoseData.cow_face_pose.duration + PoseData.corpse_pose.duration

    val backpain_yoga = Yoga("Backpain", "Yoga can greatly benefit individuals with back pain. Regular practice of yoga helps improve flexibility, reducing stiffness and tension in the back. It also strengthens the core muscles, providing support to the spine and relieving strain. Yoga promotes better posture, aligning the body and reducing stress on the spine. Breathing techniques and meditation in yoga aid in stress reduction and relaxation, further alleviating back pain.", total_duration_backpain_yoga, R.drawable.backpain_yoga, PoseData.backpain)
    val anxiety_yoga = Yoga("Anxiety", "Yoga helps reduce anxiety by calming the mind and body. Deep breathing techniques in yoga relax the body and slow the heart rate. Yoga movements release tension and promote physical relaxation. Mindfulness and meditation in yoga increase self-awareness to manage anxiety triggers. Yoga provides a supportive environment that reduces feelings of isolation and anxiety. By combining breathwork, movement, mindfulness, and support, yoga effectively manages anxiety.", total_duration_anxiety_yoga, R.drawable.anxiety_yoga, PoseData.anxiety)
    val flexibility_yoga = Yoga("Flexibility", "Yoga improves flexibility by targeting different muscles, reducing tightness and stiffness. Regular practice enhances range of motion and suppleness in the body, leading to better physical performance and reducing the risk of injuries. Yoga emphasizes proper alignment and body awareness, allowing gradual and safe progress in flexibility. Improved flexibility positively impacts daily activities and overall well-being. It's important to practice yoga with proper form, alignment, and respect for the body's limitations to avoid injuries.", total_duration_flexibility_yoga, R.drawable.flexibility_yoga, PoseData.flexibility)
    val neckpain_yoga = Yoga("Neck Pain", "Yoga relieves neck pain by improving posture, releasing tension, and reducing stress. Regular practice aligns the body, reducing strain on the neck muscles. Yoga exercises enhance body awareness and maintain proper neck alignment. Stretching and strengthening target neck and shoulder muscles, increasing flexibility and releasing tension. Relaxation techniques like deep breathing and meditation reduce overall stress, alleviating neck pain caused by muscle tension. ", total_duration_neckpain_yoga, R.drawable.neck_pain_yoga, PoseData.neckPain)

    val yoga: ArrayList<Yoga>
        get() {
            val list = arrayListOf<Yoga>()
            list.add(backpain_yoga)
            list.add(anxiety_yoga)
            list.add(flexibility_yoga)
            list.add(neckpain_yoga)
            return list
        }
}