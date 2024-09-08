package com.example.weather_app.domain.usecase.weather

import com.example.weather_app.domain.repository.retrofit.DailyWeatherRepository
import javax.inject.Inject

class DailyWeatherDataUseCase @Inject constructor (private val repository: DailyWeatherRepository) {
    suspend operator fun invoke(
        numOfRows: Int,
        pageNo: Int,
        regId: String,
        tmFc: String
    ) = repository.getDailyWeatherData(numOfRows, pageNo, regId, tmFc)
}