package com.newsapp.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.paging.*
import com.newsapp.R
import com.newsapp.adapter.NewsAdapter
import com.newsapp.data.repo.NewsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsViewModel(
    application: Application,
    private val adapter: NewsAdapter
) : AndroidViewModel(application) {

    private val _searchContent = MutableLiveData<String>()

    private val _progressBarVisibility = MutableLiveData(View.GONE)
    val progressBarVisibility: LiveData<Int> get() = _progressBarVisibility

    private val _emptyNewsWarningVisibility = MutableLiveData(View.VISIBLE)
    val emptyNewsWarningVisibility: LiveData<Int> get() = _emptyNewsWarningVisibility

    private val _emptyNewsWarningText =
        MutableLiveData(getApplication<Application>().getString(R.string.search_something))
    val emptyNewsWarningText: LiveData<String> get() = _emptyNewsWarningText

    init {
        setAdapterLoadStateListener()
    }

    private fun setAdapterLoadStateListener() {
        // verinin durumuna göre news fragment sayfası için ayarlamalar yapar
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                _progressBarVisibility.value = View.VISIBLE
                _emptyNewsWarningVisibility.value = View.GONE
            } else {
                _progressBarVisibility.value = View.GONE
                if (adapter.itemCount == 0) {
                    _emptyNewsWarningVisibility.value = View.VISIBLE
                }
                val error = when {
                    loadState.prepend is LoadState.Error -> {
                        loadState.prepend as LoadState.Error
                    }
                    loadState.append is LoadState.Error -> {
                        loadState.append as LoadState.Error
                    }
                    loadState.refresh is LoadState.Error -> {
                        loadState.refresh as LoadState.Error
                    }
                    else -> null
                }
                // kullanıcıya yansıtılacak mesajlar ayarlanır
                handleMessage(error)
            }
        }
    }

    private fun handleMessage(error: LoadState.Error?) {
        when {
            error?.error?.javaClass.toString() == "class java.lang.NullPointerException" -> {
                _emptyNewsWarningText.value =
                    getApplication<Application>().getString(R.string.search_something)
            }
            error?.error?.javaClass.toString() == "class java.net.UnknownHostException" -> {
                _emptyNewsWarningText.value =
                    getApplication<Application>().getString(R.string.network_error)
            }
            else -> {
                _emptyNewsWarningText.value =
                    getApplication<Application>().getString(R.string.no_results)
            }
        }
    }

    private fun setSearchContent(content: String) {
        _searchContent.value = content
    }

    fun listenSearchView(query: String?): Boolean {
        setSearchContent(query.toString())
        viewModelScope.launch {
            NewsRepository(_searchContent.value.toString(), viewModelScope).getNewsData()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
        return true
    }

}



