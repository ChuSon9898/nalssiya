package com.example.weather_app.data.model

data class DailyDataModel(
    val day: String,        //요일
    val date: String,       //날짜
    val weather: String,    //날씨
    val maxTemp: String,    //최고 온도
    val minTemp: String     //최저 온도
)

data class TempDaily(
    val fcstDate: String,
    val fcstTime: String,
    val maxTemp: String,
    val minTemp: String,
    val sky: String,
    val rainType: String
)