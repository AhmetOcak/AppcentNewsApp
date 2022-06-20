package com.newsapp.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsapp.db.FavouriteNewsDatabase
import com.newsapp.utilities.Constants
import com.newsapp.view.NewsDetailFragment
import com.newsapp.viewmodel.NewsDetailViewModel
import java.lang.IllegalArgumentException

class NewsDetailViewModelFactory(
    private val application: Application,
    private val favoriteNewsDB: FavouriteNewsDatabase,
    private val fragment: NewsDetailFragment,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            return NewsDetailViewModel(application, favoriteNewsDB, fragment) as T
        }else {
            throw IllegalArgumentException(Constants.VM_FACTORY_EXCEPTION_MESSAGE)
        }
    }
}