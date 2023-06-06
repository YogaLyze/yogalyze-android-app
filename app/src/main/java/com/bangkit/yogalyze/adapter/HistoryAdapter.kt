package com.bangkit.yogalyze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.api.response.HistoryResponse
import com.bangkit.yogalyze.model.Pose

class HistoryAdapter(private val listHistory: ArrayList<HistoryResponse>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val yogaName: TextView = itemView.findViewById(R.id.tv_yoga_title)
        val poseName: TextView = itemView.findViewById(R.id.tv_yoga_pose)
        val date : TextView = itemView.findViewById(R.id.yogaDate)
        val accuracy : TextView = itemView.findViewById(R.id.accuracyTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.history_item_view, parent, false)
        return HistoryAdapter.ListViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

    }
}