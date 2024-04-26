package com.example.weather_app.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather_app.data.room.BookmarkDAO
import com.example.weather_app.data.room.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 3)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao() : BookmarkDAO
}