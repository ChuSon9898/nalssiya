package com.example.weather_app.data.model

data class DailyDataModel (
    val day : String,
    val date : String,
    val weather : String,
    val maxTemp : Int,
    val minTemp : Int
)