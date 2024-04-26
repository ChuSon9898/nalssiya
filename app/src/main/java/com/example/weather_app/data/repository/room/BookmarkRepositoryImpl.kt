package com.example.weather_app.data.repository.room

import com.example.weather_app.data.room.BookmarkDatabase
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.domain.repository.room.BookmarkRepository

class BookmarkRepositoryImpl() : BookmarkRepository {

    override fun getListAll(db : BookmarkDatabase) = db.bookmarkDao().getAllData()

    override fun insertData(db : BookmarkDatabase, location: String, nx: String, ny: String, landArea: String, tempArea: String) = db.bookmarkDao().insert(
        BookmarkEntity(0, location, nx, ny, landArea, tempArea)
    )

    override fun getDataByLocation(db: BookmarkDatabase, sLocation: String) = db.bookmarkDao().getDatabylocation(sLocation)

    override fun deleteAllData(db : BookmarkDatabase) = db.bookmarkDao().deleteAllData()

    override fun deleteData(db : BookmarkDatabase, id : Int, location: String, nx: String, ny: String, landArea: String, tempArea: String) = db.bookmarkDao().deleteData(
        BookmarkEntity(id, location, nx, ny, landArea, tempArea)
    )

}