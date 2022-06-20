package com.newsapp.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsapp.adapter.NewsAdapter
import com.newsapp.utilities.Constants
import com.newsapp.viewmodel.NewsViewModel
import java.lang.IllegalArgumentException

class NewsViewModelFactory(
    private val application: Application,
    private val adapter: NewsAdapter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return  NewsViewModel(application, adapter) as T
        }else {
            throw IllegalArgumentException(Constants.VM_FACTORY_EXCEPTION_MESSAGE)
        }
    }

}