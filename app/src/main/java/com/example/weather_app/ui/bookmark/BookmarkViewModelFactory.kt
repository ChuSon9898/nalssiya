package com.example.weather_app.ui.bookmark

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_app.data.repository.room.BookmarkRepositoryImpl

class BookmarkViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    private val bookmarkRepository = BookmarkRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(bookmarkRepository, application) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}