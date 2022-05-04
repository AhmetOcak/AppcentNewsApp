package com.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    // arama içeriğini tutar
    private var _searchContent = MutableLiveData("")
    val searchContent: LiveData<String> get() = _searchContent

    fun setSharedSearchContent(content: String) {
        _searchContent.value = content
    }
}