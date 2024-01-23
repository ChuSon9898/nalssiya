package com.example.weather_app.data.retrofit

import retrofit2.http.Query

class DailyTempRepository {
    private val client = RetrofitClient.getInstance().create(RetrofitInterface::class.java)

    suspend fun getDailyTempData(
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) = client.getDailyTemp("JSON", numOfRows, pageNo, regId, tmFc)
}