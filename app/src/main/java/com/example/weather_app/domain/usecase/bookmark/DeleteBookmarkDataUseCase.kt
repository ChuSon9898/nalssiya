package com.example.weather_app.domain.usecase.bookmark

import com.example.weather_app.domain.repository.room.BookmarkRepository
import javax.inject.Inject

class DeleteBookmarkDataUseCase @Inject constructor (private val repository: BookmarkRepository) {
    operator fun invoke(
        id: Int,
        location: String,
        nx: String,
        ny: String,
        landArea: String,
        tempArea: String
    ) = repository.deleteData(id, location, nx, ny, landArea, tempArea)
}