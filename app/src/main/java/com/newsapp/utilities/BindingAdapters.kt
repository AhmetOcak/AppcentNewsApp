package com.newsapp.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.newsapp.R
import okhttp3.HttpUrl.Companion.toHttpUrl

// news detail sayfasında yer alan imageView'ın image'ini atıyoruz
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(
            imgUrl.toHttpUrl()
        ) {
            error(R.drawable.error_image)
        }
    }
}