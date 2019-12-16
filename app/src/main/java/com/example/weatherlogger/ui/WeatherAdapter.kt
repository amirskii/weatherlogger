package com.example.weatherlogger.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.models.Temperature
import kotlinx.android.synthetic.main.layout_temperature_item.view.*

class WeatherAdapter internal constructor() : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private var mValues: ArrayList<Temperature> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bindData(item)

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_temperature_item, parent, false)) {

        private val dateTv: TextView = itemView.dateTv
        private val tempTv: TextView = itemView.tempTv
        private val moreTv: TextView = itemView.moreDetailsTv

        fun bindData(item: Temperature) {
            dateTv.text = item.at.toString()
            tempTv.text = item.temperature.toString()
        }
    }

    fun updateAll(newValues: List<Temperature>) {
        mValues.clear()
        mValues.addAll(newValues)
        notifyDataSetChanged()
    }

    fun add(newValue: Temperature) {
        mValues.add(newValue)
        notifyItemInserted(mValues.size - 1)
    }

    fun clear() {
        mValues.clear()
        notifyDataSetChanged()
    }
}

