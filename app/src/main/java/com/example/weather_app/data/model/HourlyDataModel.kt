package com.example.weather_app.data.model

//시간별 일기예보에 쓰여지는 데이터 구조
data class HourlyDataModel(
    val fcstDate: String,   //날짜
    val fcstTime: String,   //시간
    val temp: String,       //1시간 기온
    val minTemp: String,    //일일 최저 기온
    val maxTemp: String,    //일일 최고 기온
    val sky: String,        //하늘 상태
    val rainType: String,   //강수 형태
    val humidity: String,   //습도
    val rainHour : String,  //1시간 강수량
    val snowHour : String,  //1시간 적설량
    val windDir: String,    //풍향
    val windSpd : String    //풍속
)

//API로 받아오는 데이터 구조
data class HourlyWeather(
    val response: Response
) {

    data class Response(
        val header: Header,
        val body: Body
    )

    data class Header(
        val resultCode: String,
        val resultMsg: String
    )

    data class Body(
        val dataType: String,
        val items: Items
    )

    data class Items(
        val item: List<Item>
    )

    data class Item(
        val baseDate: String,
        val baseTime: String,
        val category: String,
        val fcstDate : String,
        val fcstTime : String,
        val fcstValue : String,
        val nx : Int,
        val ny : Int
    )
}