package com.example.weather_app.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weather_app.R
import com.example.weather_app.data.model.HourlyDataModel
import com.example.weather_app.databinding.HourlyItemBinding

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
            HourlyItemBinding.inflate(
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

    class ViewHolder(private val binding: HourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item : HourlyDataModel) = with(binding) {
            tvTime.text = checkAMPM(item.time)
            when(item.weather) {
                "맑음" -> ivWeather.load(R.drawable.ic_sun)
                "구름" -> ivWeather.load(R.drawable.ic_cloud)
                "눈" -> ivWeather.load(R.drawable.ic_snow)
                "비" -> ivWeather.load(R.drawable.ic_rain)
                else -> {}
            }
            tvTemp.text = "${item.temperature}°"
        }

        private fun checkAMPM (time : Int) : String {
            var convertTime : String? = null

            if (time in (0..11)) convertTime = "오전 ${time}시"
            else convertTime = "오후 ${time}시"

            return convertTime
        }
    }
}