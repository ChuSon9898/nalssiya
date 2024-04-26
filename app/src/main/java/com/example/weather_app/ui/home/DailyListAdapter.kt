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
import com.example.weather_app.databinding.HomeDailyItemBinding

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
            HomeDailyItemBinding.inflate(
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

    class ViewHolder(private val binding: HomeDailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item : DailyDataModel) = with(binding) {
            when (item.day) {
                "월요일" -> tvDay.text = "월"
                "화요일" -> tvDay.text = "화"
                "수요일" -> tvDay.text = "수"
                "목요일" -> tvDay.text = "목"
                "금요일" -> tvDay.text = "금"
                "토요일" -> tvDay.text = "토"
                "일요일" -> tvDay.text = "일"
            }
            tvDate.text = item.date
            when(item.weather) {
                "맑음" -> ivWeather.load(R.drawable.ic_sun)
                "대체로 흐림" -> ivWeather.load(R.drawable.ic_sun_and_cloud)
                "흐림" -> ivWeather.load(R.drawable.ic_cloud)
                "눈비" -> ivWeather.load(R.drawable.ic_snow_rain)
                "비" -> ivWeather.load(R.drawable.ic_rain)
                "눈" -> ivWeather.load(R.drawable.ic_snow)
            }
            tvMaxtemp.text = "${item.maxTemp}°"
            tvMintemp.text = "${item.minTemp}°"
        }
    }
}