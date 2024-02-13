package com.example.weather_app.data.repository.retrofit

import com.example.weather_app.data.retrofit.RetrofitClient
import com.example.weather_app.data.retrofit.RetrofitInterface
import com.example.weather_app.domain.repository.retrofit.HourlyRepository


class HourlyRepositoryImpl : HourlyRepository {
    override suspend fun getHourlyData(
        client: RetrofitInterface,
        numOfRows: Int,
        pageNo: Int,
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String
    ) = client.getHourlyWeatehr("JSON", numOfRows, pageNo, baseDate, baseTime, nx, ny)
}