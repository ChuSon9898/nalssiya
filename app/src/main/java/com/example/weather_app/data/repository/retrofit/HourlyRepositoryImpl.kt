package com.example.weather_app.data.repository.retrofit

import com.example.weather_app.data.retrofit.RetrofitApi
import com.example.weather_app.domain.repository.retrofit.HourlyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HourlyRepositoryImpl @Inject constructor(private val api: RetrofitApi): HourlyRepository {
    override suspend fun getHourlyData(
        numOfRows: Int,
        pageNo: Int,
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String
    ) = api.getHourlyWeatehr("JSON", numOfRows, pageNo, baseDate, baseTime, nx, ny)
}