package com.example.weather_app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.DailyDataModel
import com.example.weather_app.data.model.HourlyDataModel
import com.example.weather_app.data.model.ThreeDailyModel
import com.example.weather_app.data.model.HourlyWeather
import com.example.weather_app.data.retrofit.DailyTempRepository
import com.example.weather_app.data.retrofit.DailyWeatherRepository
import com.example.weather_app.data.retrofit.HourlyRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HomeViewModel(
    private val hourlyRepository: HourlyRepository,
    private val dailyTempRepository: DailyTempRepository,
    private val dailyWeatherRepository: DailyWeatherRepository
) : ViewModel() {
    private val _hourlyList: MutableLiveData<List<HourlyDataModel>> = MutableLiveData()
    val hourlyList: LiveData<List<HourlyDataModel>> get() = _hourlyList

    private val _dailyList: MutableLiveData<List<DailyDataModel>> = MutableLiveData()
    val dailyList: LiveData<List<DailyDataModel>> get() = _dailyList

    private val _currentWeather: MutableLiveData<HourlyDataModel?> = MutableLiveData()

    val currentWeather: LiveData<HourlyDataModel?> get() = _currentWeather

    private val _currentWeather2: MutableLiveData<DailyDataModel> = MutableLiveData()

    val currentWeather2: LiveData<DailyDataModel> get() = _currentWeather2

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

    private val formatter4 = DateTimeFormatter.ofPattern("MM / dd")

    //날씨 데이터를 불러오고 해당 List에서 현재 기준 24시간 데이터 및 현재 날씨 정보를 불러오는 함수
    fun getHourlyWeather(
        nx: String,
        ny: String
    ) {
        viewModelScope.launch {
            val response = hourlyRepository.getHourlyData(
                500,
                1,
                getBaseDate(currentDateTime),
                getBaseTime(currentTime),
                nx,
                ny
            )
            Log.d("ViewModel", "${getBaseDate(currentDateTime)}, ${getBaseTime(currentTime)}")
            val list = response.body()?.response!!.body.items.item
            val groupedData = mutableMapOf<Pair<String, String>, MutableList<HourlyWeather.Item>>()

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

    //최근 3일(오늘, 내일, 모레) 날씨 데이터를 불러오는 함수
    fun getDailyWeather(nx: String, ny: String, tempArea: String, landArea: String) {
        viewModelScope.launch {
            val dailyWeatherList = mutableListOf<DailyDataModel>()
            val response = hourlyRepository.getHourlyData(
                900,
                1,
                currentDateTime.minusDays(1).format(formatter2).toInt(),
                "2300",
                nx,
                ny
            )
            val list = response.body()?.response!!.body.items.item

            val groupedData = mutableMapOf<Pair<String, String>, MutableList<HourlyWeather.Item>>()

            //불러온 데이터의 해당 날짜, 시간을 Key값으로 하여 분류
            for (item in list) {
                val key = Pair(item.fcstDate, item.fcstTime)
                if (!groupedData.containsKey(key)) {
                    groupedData[key] = mutableListOf()
                }
                groupedData[key]?.add(item)
            }

            //분류된 데이터 객체화하여 날짜, 시간별 리스트 생성
            val tempDaily = groupedData.map { (key, items) ->
                ThreeDailyModel(
                    fcstDate = key.first,
                    fcstTime = key.second,
                    minTemp = items.firstOrNull { it.category == "TMN" }?.fcstValue ?: "",
                    maxTemp = items.firstOrNull { it.category == "TMX" }?.fcstValue ?: "",
                    sky = items.firstOrNull { it.category == "SKY" }?.fcstValue ?: "",
                    rainType = items.firstOrNull { it.category == "PTY" }?.fcstValue ?: "",
                )
            }

            //최근 3일간 최소, 최대 온도 데이터
            val minTempList = list.filter { it.category == "TMN" }.map { it.fcstValue }
            val maxTempList = list.filter { it.category == "TMX" }.map { it.fcstValue }

            //최근 3일간 날씨 데이터 생성
            for (i in 0..2) {
                dailyWeatherList.add(
                    DailyDataModel(
                        convertDay(currentDateTime.plusDays(i.toLong())),
                        currentDateTime.plusDays(i.toLong()).format(formatter4),
                        representWeather(tempDaily)[i],
                        maxTempList[i],
                        minTempList[i]
                    )
                )

            }
            
            //3일~10일 날씨 데이터를 불러오는 코드 (임시 지역 코드, 수정 필요)
            val tempResponse =
                dailyTempRepository.getDailyTempData(10, 1, tempArea, getTmFc(currentTime, currentDateTime))
            val weatherResponse =
                dailyWeatherRepository.getDailyWeatherData(10, 1, landArea, getTmFc(currentTime, currentDateTime))

            val tempList = tempResponse.body()?.response!!.body.items.item
            val weatherList = weatherResponse.body()?.response!!.body.items.item

            for (i in 3..10) {
                dailyWeatherList.add(
                    DailyDataModel(
                        convertDay(currentDateTime.plusDays(i.toLong())),
                        currentDateTime.plusDays(i.toLong()).format(formatter4),
                        when (i) {
                            3 -> weatherList[0].wf3Pm
                            4 -> weatherList[0].wf4Pm
                            5 -> weatherList[0].wf5Pm
                            6 -> weatherList[0].wf6Pm
                            7 -> weatherList[0].wf7Pm
                            8 -> weatherList[0].wf8
                            9 -> weatherList[0].wf9
                            else -> weatherList[0].wf10
                        },
                        when (i) {
                            3 -> tempList[0].taMax3.toString()
                            4 -> tempList[0].taMax4.toString()
                            5 -> tempList[0].taMax5.toString()
                            6 -> tempList[0].taMax6.toString()
                            7 -> tempList[0].taMax7.toString()
                            8 -> tempList[0].taMax8.toString()
                            9 -> tempList[0].taMax9.toString()
                            else -> tempList[0].taMax10.toString()
                        },
                        when (i) {
                            3 -> tempList[0].taMin3.toString()
                            4 -> tempList[0].taMin4.toString()
                            5 -> tempList[0].taMin5.toString()
                            6 -> tempList[0].taMin6.toString()
                            7 -> tempList[0].taMin7.toString()
                            8 -> tempList[0].taMin8.toString()
                            9 -> tempList[0].taMin9.toString()
                            else -> tempList[0].taMin10.toString()
                        }
                    )
                )
            }
            Log.d("dailyTemp", tempList.toString())
            Log.d("dailyWeather", weatherList.toString())
            
            //오늘 것은 Activity에 띄우기 위해 따로 분리
            _currentWeather2.value = dailyWeatherList[0]
            //내일, 모레 데이터 일간 리스트에 전달
            _dailyList.value = dailyWeatherList.slice(1..dailyWeatherList.lastIndex)
        }
    }

    //날짜를 요일로 변환하는 함수
    private fun convertDay(time: LocalDateTime): String {
        return time.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    }

    //하루 기준 대표 날씨 지정 함수
    private fun representWeather(list: List<ThreeDailyModel>): List<String> {
        val weatherList = mutableListOf<String>()

        for (i in list.slice(0..<list.lastIndex).chunked(24)) {
            if (i.any { it.rainType == "0" }) {
                val maxSky = i.groupBy { it.sky }
                    .maxBy { it.value.size }
                    .key
                when (maxSky) {
                    "1" -> weatherList.add("맑음")
                    "3" -> weatherList.add("대체로 흐림")
                    "4" -> weatherList.add("흐림")
                }
            } else {
                val maxType = i.groupBy { it.rainType }
                    .maxBy { it.value.size }
                    .key
                when (maxType) {
                    "1", "2", "4" -> weatherList.add("비")
                    "3" -> weatherList.add("눈")
                }
            }
        }

        return weatherList.toList()
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
        return if (time.hour in 0 until 3) time.minusDays(1).format(formatter2).toInt()
        else time.format(formatter2).toInt()
    }

    //중기예보 예보시간 계산 함수(tmFc)
    private fun getTmFc(time : LocalTime, dateTime: LocalDateTime) : String {
        return if (!time.isBefore(LocalTime.of(0, 0)) && time.isBefore(LocalTime.of(6, 5))) {
            "${dateTime.minusDays(1).format(formatter2)}1800"
        }
        else if (!time.isBefore(LocalTime.of(6, 5)) && time.isBefore(LocalTime.of(18, 5))) {
            "${dateTime.format(formatter2)}0600"
        }
        else {
            "${dateTime.format(formatter2)}1800"
        }
    }
}