package com.example.weather_app.di

import com.example.weather_app.data.repository.retrofit.DailyTempRepositoryImpl
import com.example.weather_app.data.repository.retrofit.DailyWeatherRepositoryImpl
import com.example.weather_app.data.repository.retrofit.HourlyRepositoryImpl
import com.example.weather_app.data.repository.room.BookmarkRepositoryImpl
import com.example.weather_app.domain.repository.retrofit.DailyTempRepository
import com.example.weather_app.domain.repository.retrofit.DailyWeatherRepository
import com.example.weather_app.domain.repository.retrofit.HourlyRepository
import com.example.weather_app.domain.repository.room.BookmarkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun dailyTempRepository(dailyTempRepositoryImpl: DailyTempRepositoryImpl) : DailyTempRepository

    @Singleton
    @Binds
    abstract fun dailyWeatherRepository(dailyWeatherRepositoryImpl: DailyWeatherRepositoryImpl) : DailyWeatherRepository

    @Singleton
    @Binds
    abstract fun hourlyRepository(hourlyRepositoryImpl: HourlyRepositoryImpl) : HourlyRepository

    @Singleton
    @Binds
    abstract fun bookmarkRepository(bookmarkRepositoryImpl: BookmarkRepositoryImpl) : BookmarkRepository
}