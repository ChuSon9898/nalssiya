package com.example.weather_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_app.data.retrofit.HourlyRepository

class HomeViewModelFactory() : ViewModelProvider.Factory {
    private val hourlyRepository = HourlyRepository()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(hourlyRepository) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
