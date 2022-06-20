package com.newsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.newsapp.R
import com.newsapp.data.models.FavouriteNews
import com.newsapp.data.NewsDetailViewModelFactory
import com.newsapp.databinding.FragmentNewsDetailBinding
import com.newsapp.db.FavouriteNewsDatabase
import com.newsapp.viewmodel.NewsDetailViewModel

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var viewModel: NewsDetailViewModel
    private lateinit var favoriteNewsDB: FavouriteNewsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        favoriteNewsDB = FavouriteNewsDatabase.getFavouriteNewsDatabase(requireContext())!!
    }

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newsDetailFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.newsDetailsFragmentToolbar.customToolbar.inflateMenu(R.menu.toolbar_menu)

        viewModel =
            NewsDetailViewModelFactory(requireActivity().application, favoriteNewsDB, this).create(
                NewsDetailViewModel::class.java
            )
        binding.viewModel = viewModel
        setMenuClickListener()
        setFavIcon()
        setOnBackPressed()
    }

    private fun setMenuClickListener() {
        binding.newsDetailsFragmentToolbar.customToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                // haber linkini paylaştığımız alan
                R.id.share -> {
                    val sendNewsLink: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, viewModel.newsSource.value)
                        type = "text/plain"
                    }
                    val shareNewsLink = Intent.createChooser(sendNewsLink, null)
                    startActivity(shareNewsLink)
                }
                // ilgili haberin favorilere eklendiği alan
                R.id.add_favorite -> {
                    val favNews = FavouriteNews(
                        newsTitle = binding.detailNewsTitle.text.toString(),
                        newsDate = viewModel.newsDate.value.toString(),
                        newsDescription = binding.detailNewsDescription.text.toString(),
                        newsSource = binding.newsSource.text.toString(),
                        newsImageUrl = viewModel.newsImageUrl.value.toString(),
                        newsUrl = viewModel.newsUrl.value.toString()
                    )
                    viewModel.setInComingFavData(favNews)
                    viewModel.addOrDelete(it)
                }
            }
            true
        }
    }

    // ilgili haberin favori haberlerde olup olmama durumuna göre favori haber ekleme
    // iconunu ayarlar
    private fun setFavIcon() {
        if (viewModel.setFavouriteIcon()) {
            binding.newsDetailsFragmentToolbar.customToolbar.menu.findItem(R.id.add_favorite).icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_border, null)
        } else {
            binding.newsDetailsFragmentToolbar.customToolbar.menu.findItem(R.id.add_favorite).icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite, null)
        }
    }

    // news detail fragment'ının geri dönüşünü ayarlar
    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToBackScreen()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun goToBackScreen() {
        if (viewModel.navControl.value!!) {
            findNavController().navigate(R.id.action_newsDetailFragment_to_newsFragment)
        } else {
            findNavController().navigate(R.id.action_newsDetailFragment_to_favoriteNewsFragment)
        }
    }

    fun goToNextScreen() {
        findNavController().navigate(
            R.id.action_newsDetailFragment_to_newsSourceFragment,
            Bundle().apply { putString("newsUrl", viewModel.newsUrl.value) })
    }
}