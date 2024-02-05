package com.example.weather_app.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weather_app.R
import com.example.weather_app.data.model.HourlyDataModel
import com.example.weather_app.databinding.HomeHourlyItemBinding

class HourlyListAdapter () : ListAdapter<HourlyDataModel, HourlyListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<HourlyDataModel>() {
        override fun areItemsTheSame(oldItem: HourlyDataModel, newItem: HourlyDataModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HourlyDataModel, newItem: HourlyDataModel): Boolean {
            return oldItem == newItem
        }
    }
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeHourlyItemBinding.inflate(
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

    class ViewHolder(private val binding: HomeHourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item : HourlyDataModel) = with(binding) {
            tvTime.text = checkAMPM(item.fcstTime)
            when (item.rainType.toInt()) {
                0 -> {
                    when (item.sky.toInt()) {
                        1 -> {
                            if (item.fcstTime.toInt() in 600..1700) ivWeather.load(R.drawable.ic_sun)
                            else ivWeather.load(R.drawable.ic_moon)
                        }
                        3 -> {
                            if (item.fcstTime.toInt() in 600..1700) ivWeather.load(R.drawable.ic_sun_and_cloud)
                            else ivWeather.load(R.drawable.ic_moon_and_cloud)
                        }
                        4 -> ivWeather.load(R.drawable.ic_cloud)
                    }
                }
                1, 4 -> ivWeather.load(R.drawable.ic_rain)
                2 -> ivWeather.load(R.drawable.ic_snow_rain)
                3 -> ivWeather.load(R.drawable.ic_snow)
            }
            tvTemp.text = item.temp + "°"
        }

        //오전, 오후 구분 함수
        private fun checkAMPM (time : String) : String {
            return when (time.toInt()) {
                in 0..1100 -> "오전 ${time.toInt() / 100}시"
                1200 -> "오후 12시"
                else -> "오후 ${time.toInt() / 100 - 12}시"
            }
        }
    }
}