package com.example.weather_app.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.weather_app.di.RetrofitModule
import com.example.weather_app.data.retrofit.RetrofitApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitWorkManager @Inject constructor(
    private val api: RetrofitApi,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val nx = inputData.getString("nx") ?: "55"
        val ny = inputData.getString("ny") ?: "127"

        val baseDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val response = api.getHourlyWeatehr("JSON", 300, 1, baseDate.toInt(), "0200", nx, ny)

        val list = response.body()?.response!!.body.items.item

        //최저 온도
        val minTemp = list.firstOrNull { it.category == "TMN" }?.fcstValue ?: ""
        //최고 온도
        val maxTemp = list.firstOrNull { it.category == "TMX" }?.fcstValue ?: ""
        //하루 날씨
        var weatherCode = ""
        var weather = ""

        val todayList = list.filter { it.fcstDate == baseDate }

        if (todayList.any { it.category == "PTY" && it.fcstValue != "0" }) {
            weatherCode = todayList.filter { it.category == "PTY" && it.fcstValue != "0" }
                .groupBy { it.fcstValue }.maxBy { it.value.size }.key

            when (weatherCode) {
                "1", "4" -> weather = "비"
                "2" -> weather = "눈비"
                "3" -> weather = "눈"
            }
        }
        else {
            weatherCode = todayList.filter { it.category == "SKY" }.groupBy { it.fcstValue }.maxBy { it.value.size }.key

            when (weatherCode) {
                "1" -> weather = "맑음"
                "3" -> weather = "대체로 흐림"
                "4" -> weather = "흐림"
            }
        }

        val outputData = Data.Builder()
            .putStringArray("weatherData", arrayOf(weather, minTemp, maxTemp, nx, ny))
            .build()

        return Result.success(outputData)
    }

}