package com.example.weather_app.domain.usecase.weather

import com.example.weather_app.domain.repository.retrofit.DailyTempRepository
import javax.inject.Inject

class DailyTempDataUseCase @Inject constructor (private val repository: DailyTempRepository) {
    suspend operator fun invoke(
        numOfRows: Int,
        pageNo: Int,
        regId: String,
        tmFc: String
    ) = repository.getDailyTempData(numOfRows, pageNo, regId, tmFc)
}