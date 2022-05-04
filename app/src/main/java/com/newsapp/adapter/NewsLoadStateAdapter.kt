package com.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.databinding.FragmentNewsBinding


class NewsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<NewsLoadStateAdapter.NewsLoadStateViewHolder>() {

    class NewsLoadStateViewHolder(binding: FragmentNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val searchView: SearchView = binding.searchView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NewsLoadStateViewHolder {
        return NewsLoadStateViewHolder(
            FragmentNewsBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: NewsLoadStateViewHolder, loadState: LoadState) {
        holder.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                retry.invoke()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}