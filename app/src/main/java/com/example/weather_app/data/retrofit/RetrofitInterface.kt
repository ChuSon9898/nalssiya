package com.example.weather_app.data.retrofit

import com.example.weather_app.data.model.DailyTemp
import com.example.weather_app.data.model.DailyWeather
import com.example.weather_app.data.model.HourlyWeather
import com.example.weather_app.data.retrofit.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${API_KEY}")
    suspend fun getHourlyWeatehr(
        @Query("dataType") dataType : String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("base_date") baseDate : Int,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ) : Response<HourlyWeather>

    @GET("MidFcstInfoService/getMidTa?serviceKey=${API_KEY}")
    suspend fun getDailyTemp(
        @Query("dataType") dataType: String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("regId") regId : String,
        @Query("tmFc") tmFc : String
    ) : Response<DailyTemp>

    @GET("MidFcstInfoService/getMidLandFcst?serviceKey=${API_KEY}")
    suspend fun getDailyWeather(
        @Query("dataType") dataType: String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("regId") regId : String,
        @Query("tmFc") tmFc : String
    ) : Response<DailyWeather>
}