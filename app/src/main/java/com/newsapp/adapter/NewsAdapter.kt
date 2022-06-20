package com.newsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.newsapp.R
import com.newsapp.databinding.NewsCardBinding
import com.newsapp.data.models.ArticlesModel
import okhttp3.HttpUrl.Companion.toHttpUrl


class NewsAdapter(private val findNavController: NavController) :
    PagingDataAdapter<ArticlesModel, NewsAdapter.NewsViewHolder>(DiffUtilCallBack()) {

    class NewsViewHolder(binding: NewsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val newsTitle: TextView = binding.newsTitle
        val newsDescription: TextView = binding.newsDescription
        val imageView: ImageView = binding.newsImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(NewsCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.newsTitle.text = article.title
            holder.newsDescription.text = article.description
            holder.imageView.load(article.urlToImage?.toHttpUrl())

            // tıklanan haberin detay sayfasına haberin verileri ile beraber gider
            holder.itemView.setOnClickListener {
                findNavController.navigate(
                    R.id.action_newsFragment_to_newsDetailFragment,
                    Bundle().apply {
                        putSerializable("newsData", article)
                    })
            }
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<ArticlesModel>() {
    override fun areItemsTheSame(oldItem: ArticlesModel, newItem: ArticlesModel): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: ArticlesModel, newItem: ArticlesModel): Boolean {
        return oldItem == newItem
    }
}