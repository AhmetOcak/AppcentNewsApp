package com.newsapp.data

import androidx.room.*
import com.newsapp.data.models.FavouriteNews

@Dao
interface FavouriteNewsDao {

    @Insert
    fun addFavouriteNews(favoriteNews: FavouriteNews)

    @Query("SELECT * FROM favourite_news")
    fun getAllFavouriteNews(): List<FavouriteNews>

    @Query("SELECT * FROM favourite_news WHERE news_url = :newsUrl")
    fun getFavouriteNews(newsUrl: String): FavouriteNews?

    @Query("DELETE FROM favourite_news WHERE news_url = :newsUrl")
    fun deleteFavouriteNews(newsUrl: String)
}