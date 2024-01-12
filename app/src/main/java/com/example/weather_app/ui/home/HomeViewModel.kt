package com.example.weather_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather_app.data.model.DailyDataModel
import com.example.weather_app.data.model.HourlyDataModel

class HomeViewModel : ViewModel() {
    private val _hourlyList: MutableLiveData<List<HourlyDataModel>> = MutableLiveData()
    val hourlyList: LiveData<List<HourlyDataModel>> get() = _hourlyList

    private val _dailyList: MutableLiveData<List<DailyDataModel>> = MutableLiveData()
    val dailyList: LiveData<List<DailyDataModel>> get() = _dailyList

    private val hourlyDummyData: List<HourlyDataModel> = listOf(
        HourlyDataModel(11, "맑음", 4),
        HourlyDataModel(12, "맑음", 6),
        HourlyDataModel(13, "구름", 7),
        HourlyDataModel(14, "구름", 7),
        HourlyDataModel(15, "구름", 5),
        HourlyDataModel(16, "구름", 4),
        HourlyDataModel(17, "맑음", 4),
        HourlyDataModel(18, "맑음", 2),
        HourlyDataModel(19, "맑음", 0),
        HourlyDataModel(20, "맑음", 0)
    )

    private val dailyDummyData: List<DailyDataModel> = listOf(
        DailyDataModel("오늘", "01.10", "맑음", 4, -3),
        DailyDataModel("목", "01.11", "맑음", 4, -3),
        DailyDataModel("금", "01.12", "구름", 0, -5),
        DailyDataModel("토", "01.13", "구름", -1, -5),
        DailyDataModel("일", "01.14", "눈", -2, -7),
        DailyDataModel("월", "01.15", "구름", 0, -4),
        DailyDataModel("화", "01.16", "눈", -4, -8),
        DailyDataModel("수", "01.17", "구름", 1, -3),
        DailyDataModel("목", "01.18", "맑음", 4, 0),
        DailyDataModel("금", "01.19", "맑음", 5, 1)
    )

    init {
        _hourlyList.value = hourlyDummyData
        _dailyList.value = dailyDummyData
    }
}