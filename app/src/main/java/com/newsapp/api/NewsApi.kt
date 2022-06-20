package com.newsapp.api

import com.newsapp.data.models.NewsModel
import com.newsapp.utilities.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(Constants.END_POINT)
    suspend fun getNews(
        @Query(Constants.KEYWORD) keyword: String,
        @Query(Constants.PAGE) page: String,
        @Query(Constants.KEY) apiKey: String
    ): Response<NewsModel>
}