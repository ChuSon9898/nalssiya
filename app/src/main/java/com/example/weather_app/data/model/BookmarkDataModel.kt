package com.example.weather_app.data.model

//데이터 타입 수정 필요!
data class BookmarkDataModel(
    val id : Int,
    val location: String,
    val time: String,
    val temp: String,
    val maxMin: String
)
