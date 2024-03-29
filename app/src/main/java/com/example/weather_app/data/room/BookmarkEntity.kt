package com.example.weather_app.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_table")
data class BookmarkEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Int,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "nx")
    val nx: String,
    @ColumnInfo(name = "ny")
    val ny: String,
    @ColumnInfo(name = "landArea")
    val landArea: String,
    @ColumnInfo(name = "tempArea")
    val tempArea: String

)