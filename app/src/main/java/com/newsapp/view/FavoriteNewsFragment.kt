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
import com.newsapp.data.FavouriteNews
import com.newsapp.databinding.FragmentFavoriteNewsBinding
import com.newsapp.di.FavouriteNewsDatabase

class FavoriteNewsFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteNewsBinding
    private lateinit var favouriteNewsDB: FavouriteNewsDatabase
    private lateinit var favouriteNewsList: List<FavouriteNews>
    private lateinit var favouriteNewsAdapter: FavouriteNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteNewsDB = FavouriteNewsDatabase.getFavouriteNewsDatabase(requireContext())!!
        favouriteNewsList = favouriteNewsDB.favouriteNewsDao().getAllFavouriteNews()
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
        binding.toolbar.customToolbar.title = getString(R.string.favourite_fragment_title)
        binding.favouriteNewsRecylerview.apply {
            layoutManager = LinearLayoutManager(activity)
            favouriteNewsAdapter = FavouriteNewsAdapter(favouriteNewsList, findNavController())
            adapter = favouriteNewsAdapter
        }

        // henüz favori haber eklenmediyse kullanıcı bilgilendirilir
        if (favouriteNewsAdapter.itemCount == 0) {
            binding.emptyWarning.visibility = View.VISIBLE
        }

        // news fragment'ına dönüş yalnızca tablar araclığıyla sağlanmalıdır
        // geri tuşunun news fragment'ına yönlendirmesi engellenir
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}