package com.example.weather_app.data.model

data class DailyDataModel(
    val day: String,        //요일
    val date: String,       //날짜
    val weather: String,    //날씨
    val maxTemp: String,    //최고 온도
    val minTemp: String     //최저 온도
)

data class ThreeDailyModel(
    val fcstDate: String,
    val fcstTime: String,
    val maxTemp: String,
    val minTemp: String,
    val sky: String,
    val rainType: String
)

//API로 받아오는 데이터 구조
data class DailyTemp(
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

    data class Item (
        val regId: String,
        val taMin3: Int,
        val taMin3Low: Int,
        val taMin3High: Int,
        val taMax3: Int,
        val taMax3Low: Int,
        val taMax3High: Int,
        val taMin4: Int,
        val taMin4Low: Int,
        val taMin4High: Int,
        val taMax4: Int,
        val taMax4Low: Int,
        val taMax4High: Int,
        val taMin5: Int,
        val taMin5Low: Int,
        val taMin5High: Int,
        val taMax5: Int,
        val taMax5Low: Int,
        val taMax5High: Int,
        val taMin6: Int,
        val taMin6Low: Int,
        val taMin6High: Int,
        val taMax6: Int,
        val taMax6Low: Int,
        val taMax6High: Int,
        val taMin7: Int,
        val taMin7Low: Int,
        val taMin7High: Int,
        val taMax7: Int,
        val taMax7Low: Int,
        val taMax7High: Int,
        val taMin8: Int,
        val taMin8Low: Int,
        val taMin8High: Int,
        val taMax8: Int,
        val taMax8Low: Int,
        val taMax8High: Int,
        val taMin9: Int,
        val taMin9Low: Int,
        val taMin9High: Int,
        val taMax9: Int,
        val taMax9Low: Int,
        val taMax9High: Int,
        val taMin10: Int,
        val taMin10Low: Int,
        val taMin10High: Int,
        val taMax10: Int,
        val taMax10Low: Int,
        val taMax10High: Int
    )
}

//API로 받아오는 데이터 구조
data class DailyWeather(
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

    data class Item (
        val regId: String,
        val rnSt3Am: Int,
        val rnSt3Pm: Int,
        val rnSt4Am: Int,
        val rnSt4Pm: Int,
        val rnSt5Am: Int,
        val rnSt5Pm: Int,
        val rnSt6Am: Int,
        val rnSt6Pm: Int,
        val rnSt7Am: Int,
        val rnSt7Pm: Int,
        val rnSt8: Int,
        val rnSt9: Int,
        val rnSt10: Int,
        val wf3Am: String,
        val wf3Pm: String,
        val wf4Am: String,
        val wf4Pm: String,
        val wf5Am: String,
        val wf5Pm: String,
        val wf6Am: String,
        val wf6Pm: String,
        val wf7Am: String,
        val wf7Pm: String,
        val wf8: String,
        val wf9: String,
        val wf10: String
    )
}
