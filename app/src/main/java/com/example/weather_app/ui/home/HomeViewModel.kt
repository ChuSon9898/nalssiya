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

    private val _currentWeather: MutableLiveData<HourlyDataModel?> = MutableLiveData()

    val currentWeather: LiveData<HourlyDataModel?> get() = _currentWeather

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

            //불러온 데이터의 해당 날짜, 시간을 Key값으로 하여 분류
            for (item in list) {
                val key = Pair(item.fcstDate, item.fcstTime)
                if (!groupedData.containsKey(key)) {
                    groupedData[key] = mutableListOf()
                }
                groupedData[key]?.add(item)
            }

            //분류된 데이터 객체화하여 날짜, 시간별 리스트 생성
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

            //생성된 리스트에서 현재 시간 이후 24시간 내 데이터 필터링
            val todayHourlyData = hourlyData.filter { item ->
                val dateTimeString = "${item.fcstDate}${item.fcstTime}"
                currentDateTimeString <= dateTimeString && dateTimeString < tomorrowDateTimeString
            }

            //생성된 리스트에서 현재 시간에 해당하는 데이터 찾기
            val currentWeatherData =
                hourlyData.find { it.fcstDate == currentDateTime2 && it.fcstTime == currentDateTime3 }

            for (i in hourlyData) {
                Log.d("viewModel", "들어온 데이터 : $i")
            }
            for (i in todayHourlyData) {
                Log.d("viewModel", "필터링된 데이터 : $i")
            }
            Log.d("viewModel", "현재 날씨 데이터 : $currentWeatherData")

            _currentWeather.value = currentWeatherData
            _hourlyList.value = todayHourlyData
        }
    }

    fun getMinMaxTemp(nx: String, ny: String) {
        viewModelScope.launch {
            val response = repository.getHourlyData(
                900,
                1,
                currentDateTime.minusDays(1).format(formatter2).toInt(),
                "2300",
                nx,
                ny
            )
            val list =
                response.body()?.response!!.body.items.item.filter { it.category == "TMN" || it.category == "TMX" }
            val pairedList = list.windowed(2, 2, true) { (tmn, tmx) ->
                Pair(tmn, tmx)
            }
        }
    }

    //단기예보 BaseTime 계산 함수
    private fun getBaseTime(time: LocalTime): String {
        var baseTime = ""
        if (!time.isBefore(LocalTime.of(3, 0)) && time.isBefore(LocalTime.of(6, 0))) baseTime =
            "0200"
        else if (!time.isBefore(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(9, 0))) baseTime =
            "0500"
        else if (!time.isBefore(LocalTime.of(9, 0)) && time.isBefore(
                LocalTime.of(
                    12,
                    0
                )
            )
        ) baseTime = "0800"
        else if (!time.isBefore(LocalTime.of(12, 0)) && time.isBefore(
                LocalTime.of(
                    15,
                    0
                )
            )
        ) baseTime = "1100"
        else if (!time.isBefore(LocalTime.of(15, 0)) && time.isBefore(
                LocalTime.of(
                    18,
                    0
                )
            )
        ) baseTime = "1400"
        else if (!time.isBefore(LocalTime.of(18, 0)) && time.isBefore(
                LocalTime.of(
                    21,
                    0
                )
            )
        ) baseTime = "1700"
        else if (!time.isBefore(LocalTime.of(21, 0)) && time.isBefore(
                LocalTime.of(
                    23,
                    59,
                    59,
                )
            )
        ) baseTime = "2000"
        else if (!time.isBefore(LocalTime.of(0, 0)) && time.isBefore(
                LocalTime.of(
                    3,
                    0
                )
            )
        ) baseTime = "2300"

        return baseTime
    }

    //단기예보 BaseDate 계산 함수
    private fun getBaseDate(time: LocalDateTime): Int {
        val baseDate = time.format(formatter2).toInt()

        return if (time.hour in 0 until 3) baseDate - 1
        else baseDate
    }
}