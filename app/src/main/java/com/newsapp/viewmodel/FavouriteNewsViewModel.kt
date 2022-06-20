package com.newsapp.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapp.data.models.FavouriteNews
import com.newsapp.data.repo.FavouriteNewsRepository
import com.newsapp.db.FavouriteNewsDatabase

class FavouriteNewsViewModel(private val favouriteNewsDB: FavouriteNewsDatabase) : ViewModel() {

    private val _favouriteNewsList = MutableLiveData<List<FavouriteNews>>()
    val favouriteNewsList: LiveData<List<FavouriteNews>> get() = _favouriteNewsList

    private val _warningTextVisibility = MutableLiveData<Int>()
    val warningTextVisibility: LiveData<Int> get() = _warningTextVisibility

    private val favouriteNewsRepository: FavouriteNewsRepository =
        FavouriteNewsRepository(favouriteNewsDB)

    init {
        getFavouriteNews()
    }

    private fun getFavouriteNews() {
        _favouriteNewsList.value = favouriteNewsRepository.getFavouriteNewsData()
    }

    fun setWarningTextVisibility(count: Int) {
        if (count == 0) {
            _warningTextVisibility.value = View.VISIBLE
        } else {
            _warningTextVisibility.value = View.GONE
        }
    }
}