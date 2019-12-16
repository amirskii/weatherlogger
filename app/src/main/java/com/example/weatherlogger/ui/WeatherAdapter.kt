package com.example.weatherlogger.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.models.Weather
import kotlinx.android.synthetic.main.layout_temperature_item.view.*

class WeatherAdapter internal constructor() : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private var mValues: ArrayList<Weather> = ArrayList()

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

        fun bindData(item: Weather) {
            dateTv.text = item.at.toString()
            tempTv.text = "${item.temperature} °C"
        }
    }

    fun add(newValue: Weather) {
        mValues.add(newValue)
        notifyItemInserted(mValues.size - 1)
    }

    fun setValue(newValue: Weather) {
        if (mValues.isEmpty()) {
            mValues.add(newValue)
            notifyItemInserted(mValues.size - 1)
        } else {
            mValues[0] = newValue
            notifyItemChanged(0)
        }
    }


    fun clear() {
        mValues.clear()
        notifyDataSetChanged()
    }
}

