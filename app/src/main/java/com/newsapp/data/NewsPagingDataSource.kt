package com.newsapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapp.api.RetrofitInstance
import com.newsapp.data.models.ArticlesModel
import com.newsapp.utilities.Constants
import java.lang.Exception

class NewsPagingSource(
    private val retrofitInstance: RetrofitInstance,
    private val searchContent: String
) :
    PagingSource<Int, ArticlesModel>() {
    override fun getRefreshKey(state: PagingState<Int, ArticlesModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesModel> {
        return try {
            val position: Int = params.key ?: Constants.START_INDEX
            val response =
                retrofitInstance.api.getNews(searchContent, position.toString(), Constants.API_KEY)
            LoadResult.Page(
                data = response.body()!!.articles,
                prevKey = if (position == Constants.START_INDEX) null else position - 1,
                nextKey = if (response.body()!!.articles.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}