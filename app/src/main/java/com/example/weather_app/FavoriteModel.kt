package com.example.weather_app

//데이터 타입 수정 필요!
data class FavoriteModel(
    val id : Int,
    val location: String,
    val time: String,
    val temp: String,
    val maxMin: String
)
