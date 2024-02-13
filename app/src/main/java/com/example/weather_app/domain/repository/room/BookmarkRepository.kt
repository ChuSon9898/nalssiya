package com.example.weather_app.domain.repository.room

import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.room.BookmarkEntity

interface BookmarkRepository {
    fun getListAll(db : BookmarkDatabase) : List<BookmarkEntity>

    fun insertData(db : BookmarkDatabase, location: String, nx: String, ny: String, landArea: String, tempArea: String)

    fun getDataByLocation(db : BookmarkDatabase, sLocation: String) : List<BookmarkEntity>

    fun deleteAllData(db : BookmarkDatabase)

    fun deleteData(db : BookmarkDatabase, id : Int, location: String, nx: String, ny: String, landArea: String, tempArea: String)
}