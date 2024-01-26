package com.example.weather_app.data.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkDataModel(
    val id : Int,
    val Gu: String,
    val Dong: String,
    val nx : String,
    val ny : String,
    val landArea: String,
    val tempArea: String
) : Parcelable
