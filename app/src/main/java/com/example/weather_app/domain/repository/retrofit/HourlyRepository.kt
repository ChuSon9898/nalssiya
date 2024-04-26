package com.example.weather_app.domain.repository.retrofit

import com.example.weather_app.data.model.HourlyWeather
import com.example.weather_app.data.retrofit.RetrofitApi
import retrofit2.Response

interface HourlyRepository {
    suspend fun getHourlyData(
        numOfRows: Int,
        pageNo: Int,
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String
    ) : Response<HourlyWeather>
}