package com.example.weather_app.data.repository.retrofit

import com.example.weather_app.data.retrofit.RetrofitInterface
import com.example.weather_app.domain.repository.retrofit.DailyWeatherRepository

class DailyWeatherRepositoryImpl : DailyWeatherRepository {
    override suspend fun getDailyWeatherData(
        client : RetrofitInterface,
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) = client.getDailyWeather("JSON", numOfRows, pageNo, regId, tmFc)
}