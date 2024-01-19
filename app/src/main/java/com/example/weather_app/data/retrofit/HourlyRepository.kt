package com.example.weather_app.data.retrofit


class HourlyRepository {
    private val client = HourlyWeatherClient.getInstance().create(HourlyInterface::class.java)

    suspend fun getHourlyData(
        numOfRows: Int,
        pageNo: Int,
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String
    ) = client.getWeather("JSON", numOfRows, pageNo, baseDate, baseTime, nx, ny)
}