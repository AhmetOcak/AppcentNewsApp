package com.newsapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.newsapp.R
import com.newsapp.data.models.ArticlesModel
import com.newsapp.data.models.FavouriteNews
import com.newsapp.db.FavouriteNewsDatabase
import com.newsapp.utilities.Constants
import com.newsapp.view.NewsDetailFragment
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class NewsDetailViewModel(
    application: Application,
    private val favoriteNewsDB: FavouriteNewsDatabase,
    private val fragment: NewsDetailFragment,
) : AndroidViewModel(application) {

    private val _newsTitle = MutableLiveData<String>()
    val newsTitle: LiveData<String> get() = _newsTitle

    private val _newsDate = MutableLiveData<String>()
    val newsDate: LiveData<String> get() = _newsDate

    private val _newsSource = MutableLiveData<String>()
    val newsSource: LiveData<String> get() = _newsSource

    private val _newsDescription = MutableLiveData<String>()
    val newsDescription: LiveData<String> get() = _newsDescription

    private val _newsImageUrl = MutableLiveData<String>()
    val newsImageUrl: LiveData<String> get() = _newsImageUrl

    private val _newsUrl = MutableLiveData<String>()
    val newsUrl: LiveData<String> get() = _newsUrl

    // true ise newsFragmenttan gelindi, false ise favouriteFragmenttan gelindi
    val navControl = MutableLiveData<Boolean>()

    private val _favouriteNewsData = MutableLiveData<FavouriteNews>()
    private val _incomingFavNewsData = MutableLiveData<FavouriteNews>()

    private val imageNotFound: String = Constants.IMAGE_NOT_FOUND
    private val dateNotFound: String = Constants.DATE_NOT_FOUND
    private var data: Any? = null

    init {
        takeData()
        setData()
    }

    private fun addFavouriteNews(favNews: FavouriteNews) {
        favoriteNewsDB.favouriteNewsDao().addFavouriteNews(favNews)
    }

    private fun deleteFavouriteNews(newsUrl: String) {
        favoriteNewsDB.favouriteNewsDao().deleteFavouriteNews(newsUrl)
    }

    private fun getFavouriteNews() {
        _favouriteNewsData.value =
            favoriteNewsDB.favouriteNewsDao()
                .getFavouriteNews(
                    _incomingFavNewsData.value?.newsUrl.toString()
                )
    }

    fun setInComingFavData(favNewsData: FavouriteNews) {
        _incomingFavNewsData.value = favNewsData
    }

    private fun takeData() {
        if (fragment.requireArguments().getSerializable("favouriteNews") != null) {
            navControl.value = false
            data =
                fragment.requireArguments().getSerializable("favouriteNews") as FavouriteNews
        } else {
            navControl.value = true
            data =
                fragment.requireArguments().getSerializable("newsData") as ArticlesModel
        }
    }

    private fun setData() {
        if (data != null && data is FavouriteNews) {
            navControl.value = false
            setFavouriteNewsInfos(data as FavouriteNews)
        } else {
            navControl.value = true
            setNewsInfos(data as ArticlesModel)
        }
    }

    // favori haberlerim sayfasından haber detay sayfasına giderken kullanılıyor
    @SuppressLint("NewApi")
    private fun setFavouriteNewsInfos(favouriteNews: FavouriteNews) {
        _newsTitle.value = favouriteNews.newsTitle
        _newsDate.value = favouriteNews.newsDate
        _newsSource.value = favouriteNews.newsSource
        _newsDescription.value = favouriteNews.newsDescription
        _newsImageUrl.value = favouriteNews.newsImageUrl
        _newsUrl.value = favouriteNews.newsUrl
    }

    // haberler sayfasından haber detay sayfasına giderken kullanılıyor
    @SuppressLint("NewApi")
    private fun setNewsInfos(news: ArticlesModel) {
        _newsTitle.value = news.title ?: ""
        _newsDate.value = formatDate(news.publishedAt ?: "")
        _newsSource.value = news.source?.name
        _newsDescription.value = news.description ?: ""
        _newsImageUrl.value = news.urlToImage ?: imageNotFound
        _newsUrl.value = news.url ?: Constants.NOT_FOUND_PAGE
    }

    // Eğer ilgili haber daha önceden favorilere eklenmediyse favorilere ekler ve favori ekle iconunu
    // değiştirir. Eğer daha önceden favorilere eklendiyse favorilerden kaldırır ve favori ekle
    // iconun değiştirir.
    fun addOrDelete(
        it: MenuItem,
    ) {
        if (searchInDb()) {
            addFavouriteNews(_incomingFavNewsData.value!!)
            it.setIcon(R.drawable.ic_baseline_favorite)
        } else {
            deleteFavouriteNews(_incomingFavNewsData.value?.newsUrl.toString())
            it.setIcon(R.drawable.ic_baseline_favorite_border)
        }
    }

    // haberin favorilere daha önceden eklenip eklenmediği durumuna göre favori ekle iconunu düzenler
    fun setFavouriteIcon(): Boolean {
        return if (data == null) {
            searchInDb()
        } else {
            val url = favoriteNewsDB.favouriteNewsDao().getFavouriteNews(
                if (data is FavouriteNews) {
                    (data as FavouriteNews).newsUrl
                } else {
                    (data as ArticlesModel).url.toString()
                }
            )
            url == null
        }
    }

    // haberin favorilere daha önceden eklenip eklenmediğini haberin url'si aracılığıyla kontrol eder
    private fun searchInDb(): Boolean {
        getFavouriteNews()
        return _favouriteNewsData.value?.newsUrl == null
    }

    // cihazın api leveline göre elimizdeki tarih bilgisini convert ediyoruz
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date: String): String {
        return if (date != "") {
            if (Build.VERSION_CODES.O >= 26) {
                val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                formatter.format(parser.parse(date))
            } else {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
                formatter.format(parser.parse(date))
            }
        } else {
            dateNotFound
        }
    }
}