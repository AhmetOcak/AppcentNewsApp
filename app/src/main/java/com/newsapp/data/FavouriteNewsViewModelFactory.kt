package com.newsapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsapp.db.FavouriteNewsDatabase
import com.newsapp.utilities.Constants
import com.newsapp.viewmodel.FavouriteNewsViewModel

class FavouriteNewsViewModelFactory(private val favouriteNewsDB: FavouriteNewsDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteNewsViewModel::class.java)) {
            return FavouriteNewsViewModel(favouriteNewsDB) as T
        }else {
            throw IllegalArgumentException(Constants.VM_FACTORY_EXCEPTION_MESSAGE)
        }
    }
}