package com.bangkit.yogalyze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.model.Pose


class PoseAdapter(private val listPose: ArrayList<Pose>) : RecyclerView.Adapter<PoseAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.asana_item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPose.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val pose = listPose[position]

        holder.tvName.text = pose.name
        holder.tvDuration.text = pose.duration.toString()
        holder.image.setImageResource(pose.image)
        holder.tvShanskritName.text = pose.shanskrit

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listPose[holder.adapterPosition])
        }
    }

    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.poseName)
        val tvDuration: TextView = itemView.findViewById(R.id.poseDuration)
        val image : ImageView = itemView.findViewById(R.id.poseImage)
        val tvShanskritName : TextView = itemView.findViewById(R.id.poseShanskritName)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Pose)
    }

}

