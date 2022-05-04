package com.newsapp.data

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("articles") val articles : List<ArticlesModel>
)
