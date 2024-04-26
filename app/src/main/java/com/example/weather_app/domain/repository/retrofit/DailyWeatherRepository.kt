package com.example.weather_app.domain.repository.retrofit

import com.example.weather_app.data.model.DailyWeather
import com.example.weather_app.data.retrofit.RetrofitApi
import retrofit2.Response

interface DailyWeatherRepository {
    suspend fun getDailyWeatherData(
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) : Response<DailyWeather>
}