package com.example.weather_app.data.repository.retrofit

import com.example.weather_app.data.retrofit.RetrofitApi
import com.example.weather_app.domain.repository.retrofit.DailyTempRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyTempRepositoryImpl @Inject constructor(private val api: RetrofitApi) : DailyTempRepository {
    override suspend fun getDailyTempData(
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) = api.getDailyTemp("JSON", numOfRows, pageNo, regId, tmFc)
}