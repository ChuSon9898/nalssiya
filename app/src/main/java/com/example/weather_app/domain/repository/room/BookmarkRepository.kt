package com.example.weather_app.domain.repository.room

import com.example.weather_app.data.room.BookmarkEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getListAll() : Flow<List<BookmarkEntity>>

    fun insertData(location: String, nx: String, ny: String, landArea: String, tempArea: String)

    fun getDataByLocation(sLocation: String) : List<BookmarkEntity>

    fun deleteData(id : Int, location: String, nx: String, ny: String, landArea: String, tempArea: String)
}