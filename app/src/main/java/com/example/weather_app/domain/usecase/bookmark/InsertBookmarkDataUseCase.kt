package com.example.weather_app.domain.usecase.bookmark

import com.example.weather_app.domain.repository.room.BookmarkRepository
import javax.inject.Inject

class InsertBookmarkDataUseCase @Inject constructor (private val repository: BookmarkRepository) {
    operator fun invoke(
        location: String,
        nx: String,
        ny: String,
        landArea: String,
        tempArea: String
    ) = repository.insertData(location, nx, ny, landArea, tempArea)
}