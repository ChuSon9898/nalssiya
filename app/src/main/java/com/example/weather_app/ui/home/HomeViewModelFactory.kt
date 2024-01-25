package com.example.weather_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_app.data.retrofit.DailyTempRepository
import com.example.weather_app.data.retrofit.DailyWeatherRepository
import com.example.weather_app.data.retrofit.HourlyRepository

class HomeViewModelFactory() : ViewModelProvider.Factory {
    private val hourlyRepository = HourlyRepository()
    private val dailyTempRepository = DailyTempRepository()
    private val dailyWeatherRepository = DailyWeatherRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(hourlyRepository, dailyTempRepository, dailyWeatherRepository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
