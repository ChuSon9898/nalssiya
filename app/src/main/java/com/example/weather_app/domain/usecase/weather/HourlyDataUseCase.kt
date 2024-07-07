package com.example.weather_app.domain.usecase.weather

import com.example.weather_app.domain.repository.retrofit.HourlyRepository
import javax.inject.Inject

class HourlyDataUseCase @Inject constructor (private val repository: HourlyRepository) {
    suspend operator fun invoke(
        numOfRows: Int,
        pageNo: Int,
        baseDate: Int,
        baseTime: String,
        nx: String,
        ny: String
    ) = repository.getHourlyData(numOfRows, pageNo, baseDate, baseTime, nx, ny)
}