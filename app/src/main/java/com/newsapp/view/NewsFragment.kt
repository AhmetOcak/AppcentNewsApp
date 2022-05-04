package com.newsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.adapter.NewsAdapter
import com.newsapp.viewmodel.NewsFragmentViewModel
import com.newsapp.viewmodel.SharedViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var recylerViewAdapter: NewsAdapter
    private val viewModel: NewsFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner
            toolbar.customToolbar.title = getString(R.string.news_fragment_title)
        }
        binding.newsRecylerView.apply {
            layoutManager = LinearLayoutManager(activity)
            recylerViewAdapter = NewsAdapter(findNavController())
            adapter = recylerViewAdapter
        }

        // verinin durumuna göre news fragment sayfası için ayarlamalar yapar
        recylerViewAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.emptyNewsWarning.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                if (recylerViewAdapter.itemCount == 0) {
                    binding.emptyNewsWarning.visibility = View.VISIBLE
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

        // haberin detayına baktıktan sonra veya favorilere baktıktan sonra geri
        // dönüldüğünde mevcut arama sonucunu tekrar getiriyor
        if (viewModel.searchContent.value.isNullOrEmpty()) {
            binding.searchView.setQuery(sharedViewModel.searchContent.value.toString(), true)
            viewModel.setSearchContent(sharedViewModel.searchContent.value.toString())
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getNewsData().collectLatest {
                    recylerViewAdapter.submitData(it)
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // girilen içeriğe göre gelen veri işlenir
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchContent(query.toString())
                sharedViewModel.setSharedSearchContent(query.toString())
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getNewsData().collectLatest {
                        recylerViewAdapter.submitData(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun handleMessage(error: LoadState.Error?) {
        when {
            error?.error?.javaClass.toString() == "class java.lang.NullPointerException" -> {
                binding.emptyNewsWarning.text = getString(R.string.search_something)
            }
            error?.error?.javaClass.toString() == "class java.net.UnknownHostException" -> {
                binding.emptyNewsWarning.text = getString(R.string.network_error)
            }
            else -> {
                binding.emptyNewsWarning.text = getString(R.string.no_results)
            }
        }
    }
}