package com.newsapp.data.repo

import com.newsapp.data.FavouriteNews
import com.newsapp.db.FavouriteNewsDatabase

class FavouriteNewsRepository(private val favouriteNewsDB: FavouriteNewsDatabase) {

    fun getFavouriteNewsData(): List<FavouriteNews> {
        return favouriteNewsDB.favouriteNewsDao().getAllFavouriteNews()
    }

}