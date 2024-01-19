package com.example.weather_app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.DailyDataModel
import com.example.weather_app.data.model.HourlyDataModel
import com.example.weather_app.data.model.Weather
import com.example.weather_app.data.retrofit.HourlyRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeViewModel(private val repository: HourlyRepository) : ViewModel() {
    private val _hourlyList: MutableLiveData<List<HourlyDataModel>> = MutableLiveData()
    val hourlyList: LiveData<List<HourlyDataModel>> get() = _hourlyList

    private val _dailyList: MutableLiveData<List<DailyDataModel>> = MutableLiveData()
    val dailyList: LiveData<List<DailyDataModel>> get() = _dailyList

    private val _currentWeather: MutableLiveData<HourlyDataModel> = MutableLiveData()

    val currentWeather: LiveData<HourlyDataModel> get() = _currentWeather

    //임시 주석
//    private val tempDailyWeather = mutableListOf<DailyDataModel>()

    private val currentDateTime = LocalDateTime.now()
    private val tomorrowDateTime = currentDateTime.plusHours(24)
    private val currentTime = LocalTime.now()

    private val formatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    private val currentDateTimeString = currentDateTime.format(formatter1)
    private val tomorrowDateTimeString = tomorrowDateTime.format(formatter1)

    private val formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val currentDateTime2 = currentDateTime.format(formatter2)

    private val formatter3 = DateTimeFormatter.ofPattern("HH")
    private val currentDateTime3 = "${currentDateTime.format(formatter3)}00"

    //임시 주석
//    private val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.KOREAN)


    //날씨 데이터를 불러오고 해당 List에서 현재 기준 24시간 데이터 및 현재 날씨 정보를 불러오는 함수
    fun getHourlyWeather(
        nx: String,
        ny: String
    ) {
        viewModelScope.launch {
            val response = repository.getHourlyData(
                500,
                1,
                getBaseDate(currentDateTime),
                getBaseTime(currentTime),
                nx,
                ny
            )
            Log.d("ViewModel", "${getBaseDate(currentDateTime)}, ${getBaseTime(currentTime)}")
            val list = response.body()?.response!!.body.items.item
            val groupedData = mutableMapOf<Pair<String, String>, MutableList<Weather.Item>>()

            for (item in list) {
                val key = Pair(item.fcstDate, item.fcstTime)
                if (!groupedData.containsKey(key)) {
                    groupedData[key] = mutableListOf()
                }
                groupedData[key]?.add(item)
            }

            val hourlyData = groupedData.map { (key, items) ->
                HourlyDataModel(
                    fcstDate = key.first,
                    fcstTime = key.second,
                    temp = items.firstOrNull { it.category == "TMP" }?.fcstValue ?: "",
                    minTemp = items.firstOrNull { it.category == "TMN" }?.fcstValue ?: "",
                    maxTemp = items.firstOrNull { it.category == "TMX" }?.fcstValue ?: "",
                    sky = items.firstOrNull { it.category == "SKY" }?.fcstValue ?: "",
                    rainType = items.firstOrNull { it.category == "PTY" }?.fcstValue ?: "",
                    humidity = items.firstOrNull { it.category == "REH" }?.fcstValue ?: "",
                    rainHour = items.firstOrNull { it.category == "PCP" }?.fcstValue ?: "",
                    snowHour = items.firstOrNull { it.category == "SNO" }?.fcstValue ?: "",
                    windDir = items.firstOrNull { it.category == "VEC" }?.fcstValue ?: "",
                    windSpd = items.firstOrNull { it.category == "WSD" }?.fcstValue ?: ""
                )
            }

            val todayHourlyData = hourlyData.filter { item ->
                val dateTimeString = "${item.fcstDate}${item.fcstTime}"
                currentDateTimeString <= dateTimeString && dateTimeString < tomorrowDateTimeString
            }
            for (i in hourlyData) {
                Log.d("viewModel", i.toString())
            }

            _currentWeather.value = hourlyData.find { it.fcstDate == currentDateTime2 && it.fcstTime == currentDateTime3 }
            _hourlyList.value = todayHourlyData
        }
    }

    //임시 주석
//    fun getMinMaxTemp(nx: String, ny: String) {
//        viewModelScope.launch {
//            val response = repository.getHourlyData(
//                900,
//                1,
//                currentDateTime.minusDays(1).format(formatter2).toInt(),
//                "2300",
//                nx,
//                ny
//            )
//            val list = response.body()?.response!!.body.items.item.filter { it.category == "TMN" || it.category == "TMX" }
//            val pairedList = list.windowed(2, 2, true) { (tmn, tmx) ->
//                Pair(tmn, tmx)
//            }
//        }
//    }

    //단기예보 BaseTime 계산 함수
    private fun getBaseTime(time: LocalTime): String {
        var baseTime = ""
        if (time.isAfter(LocalTime.of(2, 0)) && time.isBefore(LocalTime.of(5, 0))) baseTime = "2300"
        else if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(8, 0))) baseTime =
            "0200"
        else if (time.isAfter(LocalTime.of(8, 0)) && time.isBefore(
                LocalTime.of(
                    11,
                    0
                )
            )
        ) baseTime = "0500"
        else if (time.isAfter(LocalTime.of(11, 0)) && time.isBefore(
                LocalTime.of(
                    14,
                    0
                )
            )
        ) baseTime = "0800"
        else if (time.isAfter(LocalTime.of(14, 0)) && time.isBefore(
                LocalTime.of(
                    17,
                    0
                )
            )
        ) baseTime = "1100"
        else if (time.isAfter(LocalTime.of(17, 0)) && time.isBefore(
                LocalTime.of(
                    20,
                    0
                )
            )
        ) baseTime = "1400"
        else if (time.isAfter(LocalTime.of(20, 0)) && time.isBefore(
                LocalTime.of(
                    23,
                    0
                )
            )
        ) baseTime = "1700"
        else if (time.isAfter(LocalTime.of(23, 0)) || time.isBefore(
                LocalTime.of(
                    2,
                    0
                )
            )
        ) baseTime = "2000"

        return baseTime
    }

    //단기예보 BaseDate 계산 함수
    private fun getBaseDate(time: LocalDateTime): Int {
        val baseDate = time.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
        return if (time.hour in 0..1) baseDate - 1
        else if (time.hour == 2 && time.minute in 0..14) baseDate - 1
        else baseDate
    }
}