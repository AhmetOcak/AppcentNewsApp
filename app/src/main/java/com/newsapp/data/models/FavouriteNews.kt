package com.newsapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favourite_news")
data class FavouriteNews(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "news_title")
    var newsTitle: String,

    @ColumnInfo(name = "news_date")
    var newsDate: String,

    @ColumnInfo(name = "news_source")
    var newsSource: String,

    @ColumnInfo(name = "news_description")
    var newsDescription: String,

    @ColumnInfo(name = "news_image_url")
    var newsImageUrl: String,

    @ColumnInfo(name = "news_url")
    var newsUrl: String,
) : Serializable