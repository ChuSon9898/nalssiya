package com.example.weather_app.di

import android.content.Context
import androidx.room.Room
import com.example.weather_app.data.room.BookmarkDAO
import com.example.weather_app.data.room.BookmarkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBookmarkDatabase(@ApplicationContext context: Context): BookmarkDatabase {
        return Room.databaseBuilder(
            context,
            BookmarkDatabase::class.java,
            "bookmark_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideBookmarkDAO(bookmarkDatabase: BookmarkDatabase): BookmarkDAO {
        return bookmarkDatabase.bookmarkDao()
    }
}