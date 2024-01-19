package com.example.weather_app.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookmarkEntity::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract fun bookmarkDao() : BookmarkDAO

    companion object{
        @Volatile
        private var INSTACE : BookmarkDatabase? = null

        fun getDatabase(
            context : Context
        ) : BookmarkDatabase{

            return INSTACE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java,
                    "bookmark_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTACE = instance
                instance
            }

        }
    }
}