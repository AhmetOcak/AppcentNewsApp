package com.newsapp.data.models

import com.google.gson.annotations.SerializedName

class SourceModel(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)