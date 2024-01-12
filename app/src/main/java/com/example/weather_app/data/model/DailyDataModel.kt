package com.example.weather_app.data.model

//데이터 구조 생각하기
data class DailyDataModel (
    val day : String,
    val date : String,
    val weather : String,
    val maxTemp : Int,
    val minTemp : Int
)