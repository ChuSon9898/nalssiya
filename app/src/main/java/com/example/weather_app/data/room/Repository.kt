package com.example.weather_app.data.room

import android.content.Context

class Repository(context: Context) {

    val db = BookmarkDatabase.getDatabase(context)

    fun getListAll() = db.bookmarkDao().getAllData()

    fun insertData(location: String) = db.bookmarkDao().insert(BookmarkEntity(0, location))

    fun deleteAllData() = db.bookmarkDao().deleteAllData()

    fun deleteData(id : Int, location: String) = db.bookmarkDao().deleteData(BookmarkEntity(id, location))

}