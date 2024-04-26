package com.example.weather_app.data.repository.room

import com.example.weather_app.data.room.BookmarkDAO
import com.example.weather_app.data.room.BookmarkEntity
import com.example.weather_app.domain.repository.room.BookmarkRepository
import javax.inject.Inject


class BookmarkRepositoryImpl @Inject constructor(private val bookmarkDAO: BookmarkDAO) : BookmarkRepository {

    override fun getListAll() = bookmarkDAO.getAllData()

    override fun insertData(location: String, nx: String, ny: String, landArea: String, tempArea: String) = bookmarkDAO.insert(
        BookmarkEntity(0, location, nx, ny, landArea, tempArea)
    )

    override fun getDataByLocation(sLocation: String) = bookmarkDAO.getDatabylocation(sLocation)

    override fun deleteAllData() = bookmarkDAO.deleteAllData()

    override fun deleteData(id : Int, location: String, nx: String, ny: String, landArea: String, tempArea: String) = bookmarkDAO.deleteData(
        BookmarkEntity(id, location, nx, ny, landArea, tempArea)
    )

}