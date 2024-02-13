package com.example.weather_app.data.repository.retrofit

import com.example.weather_app.data.retrofit.RetrofitClient
import com.example.weather_app.data.retrofit.RetrofitInterface
import com.example.weather_app.domain.repository.retrofit.DailyTempRepository

class DailyTempRepositoryImpl : DailyTempRepository {
    override suspend fun getDailyTempData(
        client : RetrofitInterface,
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) = client.getDailyTemp("JSON", numOfRows, pageNo, regId, tmFc)
}