package com.example.weather_app.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDAO {

    @Query("SELECT * FROM bookmark_table")
    fun getAllData() : List<BookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmark_table WHERE location = :slocation")
    fun getDatabylocation(slocation: String): List<BookmarkEntity>

    @Query("DELETE FROM bookmark_table")
    fun deleteAllData()

    @Delete
    fun deleteData(bookmark: BookmarkEntity)

}