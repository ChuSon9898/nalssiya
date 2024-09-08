package com.example.weather_app.domain.repository.retrofit

import com.example.weather_app.data.model.DailyTemp
import com.example.weather_app.data.retrofit.RetrofitApi
import retrofit2.Response

interface DailyTempRepository {
    suspend fun getDailyTempData(
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ): Response<DailyTemp>
}