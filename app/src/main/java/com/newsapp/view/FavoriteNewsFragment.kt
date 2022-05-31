package com.newsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.adapter.FavouriteNewsAdapter
import com.newsapp.data.FavouriteNewsViewModelFactory
import com.newsapp.databinding.FragmentFavoriteNewsBinding
import com.newsapp.db.FavouriteNewsDatabase
import com.newsapp.viewmodel.FavouriteNewsViewModel

class FavoriteNewsFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteNewsBinding
    private lateinit var favouriteNewsDB: FavouriteNewsDatabase
    private lateinit var favouriteNewsAdapter: FavouriteNewsAdapter
    private lateinit var viewModel: FavouriteNewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteNewsDB = FavouriteNewsDatabase.getFavouriteNewsDatabase(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            FavouriteNewsViewModelFactory(favouriteNewsDB).create(FavouriteNewsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolbar.customToolbar.title = getString(R.string.favourite_fragment_title)
        binding.favouriteNewsRecylerview.apply {
            layoutManager = LinearLayoutManager(activity)
            favouriteNewsAdapter = FavouriteNewsAdapter(
                viewModel.favouriteNewsList.value ?: arrayListOf(),
                findNavController()
            )
            adapter = favouriteNewsAdapter
        }
        viewModel.setWarningTextVisibility(favouriteNewsAdapter.itemCount)
        onBackHandler()
    }

    private fun onBackHandler() {
        // news fragment'ına dönüş yalnızca tablar araclığıyla sağlanmalıdır
        // geri tuşunun news fragment'ına yönlendirmesi engellenir
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}