package com.example.weather_app.domain.repository.retrofit

import com.example.weather_app.data.model.DailyTemp
import com.example.weather_app.data.retrofit.RetrofitInterface
import retrofit2.Response

interface DailyTempRepository {
    suspend fun getDailyTempData(
        client : RetrofitInterface,
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ): Response<DailyTemp>
}