package com.newsapp.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newsapp.data.FavouriteNews
import com.newsapp.data.FavouriteNewsDao

@Database(entities = [FavouriteNews::class], version = 1)
abstract class FavouriteNewsDatabase : RoomDatabase() {

    abstract fun favouriteNewsDao(): FavouriteNewsDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteNewsDatabase? = null

        fun getFavouriteNewsDatabase(context: Context): FavouriteNewsDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteNewsDatabase::class.java,
                    "favourites_news.db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}