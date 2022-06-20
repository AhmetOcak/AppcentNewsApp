package com.newsapp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.newsapp.api.RetrofitInstance
import com.newsapp.data.models.ArticlesModel
import com.newsapp.data.NewsPagingSource
import com.newsapp.utilities.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val searchContent: String,
    private val viewModelScope: CoroutineScope
) {
    private fun getPagingNews(): Flow<PagingData<ArticlesModel>> {
        val pager = Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                NewsPagingSource(
                    RetrofitInstance,
                    searchContent
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