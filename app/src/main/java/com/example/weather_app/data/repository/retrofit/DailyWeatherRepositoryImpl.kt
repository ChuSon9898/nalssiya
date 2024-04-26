package com.example.weather_app.data.repository.retrofit

import com.example.weather_app.data.retrofit.RetrofitApi
import com.example.weather_app.domain.repository.retrofit.DailyWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyWeatherRepositoryImpl @Inject constructor(private val api: RetrofitApi): DailyWeatherRepository {
    override suspend fun getDailyWeatherData(
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) = api.getDailyWeather("JSON", numOfRows, pageNo, regId, tmFc)
}