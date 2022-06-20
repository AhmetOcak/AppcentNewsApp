package com.newsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.newsapp.R
import com.newsapp.data.models.FavouriteNews
import com.newsapp.databinding.NewsCardBinding
import okhttp3.HttpUrl.Companion.toHttpUrl

class FavouriteNewsAdapter(
    private var favouriteNewsList: List<FavouriteNews>,
    private var findNavController: NavController
) :
    RecyclerView.Adapter<FavouriteNewsAdapter.FavouriteNewsViewHolder>() {

    class FavouriteNewsViewHolder(binding: NewsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val newsTitle: TextView = binding.newsTitle
        val newsDescription: TextView = binding.newsDescription
        val imageView: ImageView = binding.newsImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteNewsViewHolder {
        return FavouriteNewsViewHolder(NewsCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavouriteNewsViewHolder, position: Int) {
        if (favouriteNewsList.isNotEmpty()) {
            holder.newsTitle.text = favouriteNewsList[position].newsTitle
            holder.newsDescription.text = favouriteNewsList[position].newsDescription
            holder.imageView.load(favouriteNewsList[position].newsImageUrl.toHttpUrl())
            val favouriteNews = favouriteNewsList[position]

            // ilgili favori habere tıklanması halinde, haberin bilgileri ile beraber
            // news detail sayfasına gidilir
            holder.itemView.setOnClickListener {
                findNavController.navigate(
                    R.id.action_favoriteNewsFragment_to_newsDetailFragment,
                    Bundle().apply {
                        putSerializable("favouriteNews", favouriteNews)
                    })
            }
        }
    }

    override fun getItemCount() = favouriteNewsList.size
}