package com.example.weather_app.data.retrofit


class HourlyRepository {
    private val client = RetrofitClient.getInstance().create(RetrofitInterface::class.java)

    suspend fun getHourlyData(
        numOfRows: Int,
        pageNo: Int,
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String
    ) = client.getHourlyWeatehr("JSON", numOfRows, pageNo, baseDate, baseTime, nx, ny)
}