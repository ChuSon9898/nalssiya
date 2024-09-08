package com.example.weather_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.model.DailyDataModel
import com.example.weather_app.data.model.HourlyDataModel
import com.example.weather_app.data.model.HourlyWeather
import com.example.weather_app.data.model.ThreeDailyModel
import com.example.weather_app.data.repository.retrofit.DailyTempRepositoryImpl
import com.example.weather_app.data.repository.retrofit.DailyWeatherRepositoryImpl
import com.example.weather_app.data.repository.retrofit.HourlyRepositoryImpl
import com.example.weather_app.di.RetrofitModule
import com.example.weather_app.data.retrofit.RetrofitApi
import com.example.weather_app.domain.usecase.weather.DailyTempDataUseCase
import com.example.weather_app.domain.usecase.weather.DailyWeatherDataUseCase
import com.example.weather_app.domain.usecase.weather.HourlyDataUseCase
import com.example.weather_app.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hourlyDataUseCase: HourlyDataUseCase,
    private val dailyTempDataUseCase: DailyTempDataUseCase,
    private val dailyWeatherDataUseCase: DailyWeatherDataUseCase
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
            val response = hourlyDataUseCase(
                500,
                1,
                Utils.getBaseDate(currentDateTime),
                Utils.getBaseTime(currentTime),
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

            _currentWeather.value = currentWeatherData
            _hourlyList.value = todayHourlyData
        }
    }

    //최근 3일(오늘, 내일, 모레) 날씨 데이터를 불러오는 함수
    fun getDailyWeather(nx: String, ny: String, tempArea: String, landArea: String) {
        viewModelScope.launch {
            val dailyWeatherList = mutableListOf<DailyDataModel>()
            val response = hourlyDataUseCase(
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
            val minTempList = list.filter { it.category == "TMN" }.map { it.fcstValue.toDouble().toInt() }
            val maxTempList = list.filter { it.category == "TMX" }.map { it.fcstValue.toDouble().toInt() }

            //최근 3일간 날씨 데이터 생성
            for (i in 0..2) {
                dailyWeatherList.add(
                    DailyDataModel(
                        convertDay(currentDateTime.plusDays(i.toLong())),
                        currentDateTime.plusDays(i.toLong()).format(formatter4),
                        representWeather(tempDaily)[i],
                        maxTempList[i].toString(),
                        minTempList[i].toString()
                    )
                )

            }
            
            //3일~10일 날씨 데이터를 불러오는 코드
            val tempResponse =
                dailyTempDataUseCase(10, 1, tempArea, getTmFc(currentTime, currentDateTime))
            val weatherResponse =
                dailyWeatherDataUseCase(10, 1, landArea, getTmFc(currentTime, currentDateTime))

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
                    "1", "4" -> weatherList.add("비")
                    "2" -> weatherList.add("눈비")
                    "3" -> weatherList.add("눈")
                }
            }
        }

        return weatherList.toList()
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