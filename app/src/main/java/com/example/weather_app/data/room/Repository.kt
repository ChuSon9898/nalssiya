package com.example.weather_app.data.room

import android.content.Context

class Repository(context: Context) {

    val db = BookmarkDatabase.getDatabase(context)

    fun getListAll() = db.bookmarkDao().getAllData()

    fun insertData(location: String, nx: String, ny: String, landArea: String, tempArea: String) = db.bookmarkDao().insert(BookmarkEntity(0, location, nx, ny, landArea, tempArea))

    fun getDatabylocation(slocation: String) = db.bookmarkDao().getDatabylocation(slocation)

    fun deleteAllData() = db.bookmarkDao().deleteAllData()

    fun deleteData(id : Int, location: String, nx: String, ny: String, landArea: String, tempArea: String) = db.bookmarkDao().deleteData(BookmarkEntity(id, location, nx, ny, landArea, tempArea))

}