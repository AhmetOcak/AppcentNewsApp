package com.newsapp.data

import androidx.room.*

@Dao
interface FavouriteNewsDao {

    @Insert
    suspend fun addFavouriteNews(favoriteNews: FavouriteNews)

    @Query("SELECT * FROM favourite_news")
    fun getAllFavouriteNews(): List<FavouriteNews>

    @Query("SELECT * FROM favourite_news WHERE news_url = :newsUrl")
    fun getFavouriteNews(newsUrl: String): FavouriteNews?

    @Query("DELETE FROM favourite_news WHERE news_url = :newsUrl")
    suspend fun deleteFavouriteNews(newsUrl: String)
}