package com.example.weather_app.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weather_app.R
import com.example.weather_app.data.model.DailyDataModel
import com.example.weather_app.databinding.DailyItemBinding

class DailyListAdapter () : ListAdapter<DailyDataModel, DailyListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<DailyDataModel>() {
        override fun areItemsTheSame(oldItem: DailyDataModel, newItem: DailyDataModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DailyDataModel, newItem: DailyDataModel): Boolean {
            return oldItem == newItem
        }
    }
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DailyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item : DailyDataModel) = with(binding) {
            tvDay.text = item.day
            tvDate.text = item.date
            when(item.weather) {
                "맑음" -> ivWeather.load(R.drawable.ic_sun)
                "구름" -> ivWeather.load(R.drawable.ic_cloud)
                "눈" -> ivWeather.load(R.drawable.ic_snow)
                "비" -> ivWeather.load(R.drawable.ic_rain)
                else -> {}
            }
            tvMaxtemp.text = "${item.maxTemp}°"
            tvMintemp.text = "${item.minTemp}°"
        }
    }
}