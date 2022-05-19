package com.newsapp.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsapp.di.FavouriteNewsDatabase
import com.newsapp.viewmodel.NewsDetailViewModel
import java.lang.IllegalArgumentException

class NewsDetailViewModelFactory(
    private val application: Application,
    private val favoriteNewsDB: FavouriteNewsDatabase,
    private val favoriteNews: Any?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            return NewsDetailViewModel(application, favoriteNewsDB, favoriteNews) as T
        }else {
            throw IllegalArgumentException("Can not create instance of this viewModel")
        }
    }
}