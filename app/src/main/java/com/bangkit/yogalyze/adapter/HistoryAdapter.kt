package com.bangkit.yogalyze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.api.response.UserHistoryItem

class HistoryAdapter(private val listHistory: ArrayList<UserHistoryItem>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val yogaName: TextView = itemView.findViewById(R.id.tv_yoga_title)
        val poseName: TextView = itemView.findViewById(R.id.tv_yoga_pose)
        val date : TextView = itemView.findViewById(R.id.yogaDate)
        val accuracy : TextView = itemView.findViewById(R.id.tv_accuracy)
        val photo : ImageView = itemView.findViewById(R.id.yoga_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.history_item_view, parent, false)
        return ListViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = listHistory[position]

        holder.accuracy.text = "${history.score.toString()}% Accuracy"
        holder.date.text = history.date
        holder.yogaName.text = history.yogaType
        holder.poseName.text = history.yogaPose

//        if (history.yogaType.equals("Backpain")){
//            holder.photo.setImageResource(R.drawable.backpain_yoga)
//        }else if (history.yogaType.equals("Anxiety")){
//            holder.photo.setImageResource(R.drawable.anxiety_yoga)
//        }else if (history.yogaType.equals("Flexibility")){
//            holder.photo.setImageResource(R.drawable.flexibility_yoga)
//        }else if (history.yogaType.equals("Neck Pain")){
//            holder.photo.setImageResource(R.drawable.neck_pain_yoga)
//        }

        if(history.yogaPose.equals("Downward Facing Dog Pose")){
            holder.photo.setImageResource(R.drawable.downward_facing_dog_pose)
        }else if (history.yogaPose.equals("Cobra Pose")){
            holder.photo.setImageResource(R.drawable.cobra_pose)
        }else if (history.yogaPose.equals("Bridge Pose")){
            holder.photo.setImageResource(R.drawable.bridge_pose)
        }else if (history.yogaPose.equals("Child Pose")){
            holder.photo.setImageResource(R.drawable.child_pose)
        }else if (history.yogaPose.equals("Locust Pose")){
            holder.photo.setImageResource(R.drawable.locust_pose)
        }else if (history.yogaPose.equals("Tree Pose")){
            holder.photo.setImageResource(R.drawable.tree_pose)
        }else if (history.yogaPose.equals("Triangle Pose")){
            holder.photo.setImageResource(R.drawable.triangle_pose)
        }else if (history.yogaPose.equals("Standing Forward Bend")){
            holder.photo.setImageResource(R.drawable.standing_forward_bend_pose)
        }else if (history.yogaPose.equals("Fish Pose")){
            holder.photo.setImageResource(R.drawable.fish_pose)
        }else if (history.yogaPose.equals("Plow Pose")){
            holder.photo.setImageResource(R.drawable.plow_pose)
        }else if (history.yogaPose.equals("Side Strecth Pose")){
            holder.photo.setImageResource(R.drawable.side_stretch_pose)
        }else if (history.yogaPose.equals("Bow Pose")){
            holder.photo.setImageResource(R.drawable.bow_pose)
        }else if (history.yogaPose.equals("Low Lunge Pose")){
            holder.photo.setImageResource(R.drawable.low_lunge_pose)
        }else if (history.yogaPose.equals("Cow Face Pose")){
            holder.photo.setImageResource(R.drawable.cow_face_pose)
        }else if (history.yogaPose.equals("Warrior II Pose")){
            holder.photo.setImageResource(R.drawable.warrior_ii_pose)
        }else if (history.yogaPose.equals("Corpse Pose")){
            holder.photo.setImageResource(R.drawable.corpse_pose)
        }
    }
}