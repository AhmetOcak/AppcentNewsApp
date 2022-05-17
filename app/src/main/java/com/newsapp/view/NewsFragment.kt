package com.newsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.adapter.NewsAdapter
import com.newsapp.data.NewsViewModelFactory
import com.newsapp.viewmodel.NewsViewModel

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var recylerViewAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolbar.customToolbar.title = getString(R.string.news_fragment_title)

        binding.newsRecylerView.apply {
            layoutManager = LinearLayoutManager(activity)
            recylerViewAdapter = NewsAdapter(findNavController())
            adapter = recylerViewAdapter
        }

        viewModel = NewsViewModelFactory(requireActivity().application, recylerViewAdapter).create(
            NewsViewModel::class.java
        )
        binding.viewModel = viewModel
    }
}