package com.example.weather_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_app.data.repository.retrofit.DailyTempRepositoryImpl
import com.example.weather_app.data.repository.retrofit.DailyWeatherRepositoryImpl
import com.example.weather_app.data.repository.retrofit.HourlyRepositoryImpl

class HomeViewModelFactory() : ViewModelProvider.Factory {
    private val hourlyRepository = HourlyRepositoryImpl()
    private val dailyTempRepository = DailyTempRepositoryImpl()
    private val dailyWeatherRepository = DailyWeatherRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(hourlyRepository, dailyTempRepository, dailyWeatherRepository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
