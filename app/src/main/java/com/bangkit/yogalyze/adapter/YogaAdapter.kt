package com.bangkit.yogalyze.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.yogalyze.R
import com.bangkit.yogalyze.adapter.YogaAdapter.*
import com.bangkit.yogalyze.model.Yoga
import com.bangkit.yogalyze.ui.yoga_detail.YogaDetailActivity

class YogaAdapter(private var listYoga: ArrayList<Yoga>) : RecyclerView.Adapter<ListViewHolder>() {

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
            val intent = Intent(holder.itemView.context, YogaDetailActivity::class.java)
            intent.putExtra(YogaDetailActivity.EXTRA_NAME, yoga.name)
            intent.putExtra(YogaDetailActivity.EXTRA_DURATION, yoga.duration)
            intent.putExtra(YogaDetailActivity.EXTRA_IMAGE, yoga.image)
            intent.putExtra(YogaDetailActivity.EXTRA_DESCRIPTION, yoga.description)
            intent.putExtra(YogaDetailActivity.EXTRA_POSES, yoga.pose)

            val options : ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity
                )

            holder.itemView.context.startActivity(intent, options.toBundle())
        }
    }

    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.yogaName)
        val tvDuration: TextView = itemView.findViewById(R.id.yogaDuration)
        val image : ImageView = itemView.findViewById(R.id.yogaImage)
    }
}

