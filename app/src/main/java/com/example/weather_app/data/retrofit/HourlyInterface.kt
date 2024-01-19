package com.example.weather_app.data.retrofit

import com.example.weather_app.data.model.Weather
import com.example.weather_app.data.retrofit.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyInterface {
    @GET("getVilageFcst?serviceKey=${API_KEY}")
    suspend fun getWeather(
        @Query("dataType") dataType : String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("base_date") baseDate : Int,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ) : Response<Weather>
}