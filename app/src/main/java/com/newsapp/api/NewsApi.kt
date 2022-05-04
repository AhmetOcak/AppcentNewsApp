package com.newsapp.api

import com.newsapp.data.NewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything/")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("page") page: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsModel>
}