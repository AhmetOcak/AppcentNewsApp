package com.newsapp.data.models

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("articles") val articles : List<ArticlesModel>
)
