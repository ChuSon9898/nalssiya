package com.example.weather_app.data.retrofit

class DailyWeatherRepository {
    private val client = RetrofitClient.getInstance().create(RetrofitInterface::class.java)

    suspend fun getDailyWeatherData(
        numOfRows : Int,
        pageNo : Int,
        regId : String,
        tmFc : String
    ) = client.getDailyWeather("JSON", numOfRows, pageNo, regId, tmFc)
}