package com.bangkit.yogalyze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.adapter.YogaAdapter.*
import com.bangkit.yogalyze.model.Yoga

class YogaAdapter(private var listYoga: ArrayList<Yoga>) : RecyclerView.Adapter<ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun submitList(newList: ArrayList<Yoga>) {
        listYoga = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.yoga_item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listYoga.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val yoga = listYoga[position]

        holder.tvName.text = yoga.name
        holder.tvDuration.text = yoga.duration.toString()
        holder.image.setImageResource(yoga.image)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listYoga[holder.adapterPosition])
        }
    }

    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.yogaName)
        val tvDuration: TextView = itemView.findViewById(R.id.yogaDuration)
        val image : ImageView = itemView.findViewById(R.id.yogaImage)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Yoga)
    }

}

