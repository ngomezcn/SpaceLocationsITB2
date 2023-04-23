package com.example.spacelocations.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spacelocations.OnClickListener
import com.example.spacelocations.R
import com.example.spacelocations.databinding.ItemLayoutBinding
import com.example.spacelocations.MarkerR

class RecyclerAdapter (private var markers: List<MarkerR>, private val listener: OnClickListener): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println(markers[position])

        val marker = markers[position]
        with(holder){
            setListener(marker)

            binding.positionTextView.text = "Lat: ${"%.2f".format(marker.latitude)} Long: ${"%.2f".format(marker.longitude)}"
            binding.titleTextView.text = marker.title
        }
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemLayoutBinding.bind(view)

        fun setListener(launch: MarkerR){
            binding.root.setOnClickListener {
                listener.onClick(launch)
            }
        }
    }
}