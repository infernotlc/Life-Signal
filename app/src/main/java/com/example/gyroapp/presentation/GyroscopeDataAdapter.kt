package com.example.gyroapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gyroapp.data.GyroscopeData

class GyroscopeDataAdapter : ListAdapter<GyroscopeData, GyroscopeDataAdapter.GyroscopeDataViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GyroscopeDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gyroscope_data, parent, false)
        return GyroscopeDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: GyroscopeDataViewHolder, position: Int) {
        val gyroscopeData = getItem(position)
        holder.bind(gyroscopeData)
    }

    class GyroscopeDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val xTextView: TextView = itemView.findViewById(R.id.xTextView)
        private val yTextView: TextView = itemView.findViewById(R.id.yTextView)
        private val zTextView: TextView = itemView.findViewById(R.id.zTextView)

        fun bind(gyroscopeData: GyroscopeData) {
            timestampTextView.text = gyroscopeData.timestamp.toString()
            xTextView.text = "X: ${gyroscopeData.x}"
            yTextView.text = "Y: ${gyroscopeData.y}"
            zTextView.text = "Z: ${gyroscopeData.z}"
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<GyroscopeData>() {
        override fun areItemsTheSame(oldItem: GyroscopeData, newItem: GyroscopeData): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: GyroscopeData, newItem: GyroscopeData): Boolean {
            return oldItem == newItem
        }
    }
}
