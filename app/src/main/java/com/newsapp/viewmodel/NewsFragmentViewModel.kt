package com.newsapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.newsapp.api.RetrofitInstance
import com.newsapp.data.ArticlesModel
import com.newsapp.data.NewsPagingSource
import com.newsapp.utilities.Constants
import kotlinx.coroutines.flow.Flow

class NewsFragmentViewModel : ViewModel() {

    private val _searchContent = MutableLiveData<String>()
    val searchContent: LiveData<String> get() = _searchContent

    private val _data = MutableLiveData<PagingData<ArticlesModel>>()
    val data: LiveData<PagingData<ArticlesModel>> get() = _data

    fun setSearchContent(content: String) {
        _searchContent.value = content
    }

    private fun getPagingNews(): Flow<PagingData<ArticlesModel>> {
        val pager = Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                NewsPagingSource(
                    RetrofitInstance,
                    _searchContent.value.toString()
                )
            }
        ).flow
            .cachedIn(viewModelScope)
        return pager
    }

    fun getNewsData(): Flow<PagingData<ArticlesModel>> {
        return getPagingNews().cachedIn(viewModelScope)
    }
}



