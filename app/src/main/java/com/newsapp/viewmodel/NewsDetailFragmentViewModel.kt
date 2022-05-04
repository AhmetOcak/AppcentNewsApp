package com.newsapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapp.R
import com.newsapp.data.ArticlesModel
import com.newsapp.data.FavouriteNews
import com.newsapp.databinding.FragmentNewsDetailBinding
import com.newsapp.di.FavouriteNewsDatabase
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class NewsDetailFragmentViewModel : ViewModel() {

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

    private val imageNotFound: String = "https://demofree.sirv.com/nope-not-here.jpg"
    private val dateNotFound: String = "date not found"

    // favori haberlerim sayfasından haber detay sayfasına giderken kullanılıyor
    @SuppressLint("NewApi")
    fun setFavouriteNewsInfos(
        favouriteNews: FavouriteNews,
    ) {
        _newsTitle.value = favouriteNews.newsTitle
        _newsDate.value = formatDate(favouriteNews.newsDate)
        _newsSource.value = favouriteNews.newsSource
        _newsDescription.value = favouriteNews.newsDescription
        _newsImageUrl.value = favouriteNews.newsImageUrl
    }

    // haberler sayfasından haber detay sayfasına giderken kullanılıyor
    @SuppressLint("NewApi")
    fun setNewsInfos(
        newsData: ArticlesModel,
    ) {
        _newsTitle.value = newsData.title ?: ""
        _newsDate.value = formatDate(newsData.publishedAt ?: "")
        _newsSource.value = newsData.source?.name
        _newsDescription.value = newsData.description ?: ""
        _newsImageUrl.value = newsData.urlToImage ?: imageNotFound
    }

    // Eğer ilgili haber daha önceden favorilere eklenmediyse favorilere ekler ve favori ekle iconunu
    // değiştirir. Eğer daha önceden favorilere eklendiyse favorilerden kaldırır ve favori ekle
    // iconun değiştirir.
    fun addOrDelete(
        it: MenuItem,
        favNews: FavouriteNews,
        newsUrl: String,
        favoriteNewsDB: FavouriteNewsDatabase
    ) {
        if (searchInDb(newsUrl, favoriteNewsDB)) {
            favoriteNewsDB.favouriteNewsDao().addFavouriteNews(favNews)
            it.setIcon(R.drawable.ic_baseline_favorite)
        } else {
            favoriteNewsDB.favouriteNewsDao().deleteFavouriteNews(favNews.newsUrl)
            it.setIcon(R.drawable.ic_baseline_favorite_border)
        }
    }

    // haberin favorilere daha önceden eklenip eklenmediği durumuna göre favori ekle iconunu düzenler
    fun setFavouriteIcon(
        newsUrl: String,
        favoriteNewsDB: FavouriteNewsDatabase,
        binding: FragmentNewsDetailBinding,
        context: Context
    ) {
        if (searchInDb(newsUrl, favoriteNewsDB)) {
            binding.newsDetailsFragmentToolbar.customToolbar.menu.findItem(R.id.add_favorite).icon =
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_baseline_favorite_border
                )
        } else {
            binding.newsDetailsFragmentToolbar.customToolbar.menu.findItem(R.id.add_favorite).icon =
                AppCompatResources.getDrawable(context, R.drawable.ic_baseline_favorite)
        }
    }

    // haberin favorilere daha önceden eklenip eklenmediğini haberin url'si aracılığıyla kontrol eder
    private fun searchInDb(newsUrl: String, favoriteNewsDB: FavouriteNewsDatabase): Boolean {
        val favNews = favoriteNewsDB.favouriteNewsDao().getFavouriteNews(newsUrl)
        return favNews == null
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