package com.newsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.newsapp.R
import com.newsapp.data.ArticlesModel
import com.newsapp.data.FavouriteNews
import com.newsapp.databinding.FragmentNewsDetailBinding
import com.newsapp.di.FavouriteNewsDatabase
import com.newsapp.viewmodel.NewsDetailFragmentViewModel

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val viewModel: NewsDetailFragmentViewModel by viewModels()
    private lateinit var favoriteNewsDB: FavouriteNewsDatabase
    private lateinit var dataFromFavouriteNewsFragment: FavouriteNews
    private lateinit var dataFromNewsFragment: ArticlesModel
    private lateinit var newsUrl: String
    private lateinit var newsDate: String
    private lateinit var newsImgUrl: String

    // true ise newsFragmenttan gelindi, false ise favouriteFragmenttan gelindi
    private var navControl: Boolean = true


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
        binding.newsDetailFragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // toolbara menu atanıyor
        binding.newsDetailsFragmentToolbar.customToolbar.inflateMenu(R.menu.toolbar_menu)

        // gelen veriyi alıp ilgili yerlere atar
        setData()

        // ilgili haberin favori haberlerde olup olmama durumuna göre favori haber ekleme
        // iconunu ayarlar
        viewModel.setFavouriteIcon(newsUrl, favoriteNewsDB, binding, requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // news detail fragment'ının geri dönüşünü ayarlar
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToBackScreen()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.newsDetailsFragmentToolbar.customToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                // haber linkini paylaştığımız alan
                R.id.share -> {
                    val sendNewsLink: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, newsUrl)
                        type = "text/plain"
                    }
                    val shareNewsLink = Intent.createChooser(sendNewsLink, null)
                    startActivity(shareNewsLink)
                }
                // ilgili haberin favorilere eklendiği alan
                R.id.add_favorite -> {
                    val favNews = FavouriteNews(
                        newsTitle = binding.detailNewsTitle.text.toString(),
                        newsDate = newsDate,
                        newsDescription = binding.detailNewsDescription.text.toString(),
                        newsSource = binding.newsSource.text.toString(),
                        newsImageUrl = newsImgUrl,
                        newsUrl = newsUrl
                    )
                    viewModel.addOrDelete(it, favNews, newsUrl, favoriteNewsDB)
                }
            }
            true
        }
    }

    private fun setData() {
        if (requireArguments().getSerializable("favouriteNews") != null) {
            navControl = false
            dataFromFavouriteNewsFragment =
                requireArguments().getSerializable("favouriteNews") as FavouriteNews
            viewModel.setFavouriteNewsInfos(dataFromFavouriteNewsFragment)
            newsUrl = dataFromFavouriteNewsFragment.newsUrl
            newsDate = dataFromFavouriteNewsFragment.newsDate
            newsImgUrl = dataFromFavouriteNewsFragment.newsImageUrl
        } else {
            navControl = true
            dataFromNewsFragment =
                requireArguments().getSerializable("newsData") as ArticlesModel
            viewModel.setNewsInfos(dataFromNewsFragment)
            newsUrl = dataFromNewsFragment.url.toString()
            newsDate = dataFromNewsFragment.publishedAt.toString()
            newsImgUrl = dataFromNewsFragment.urlToImage.toString()
        }
    }

    private fun goToBackScreen() {
        if (navControl) {
            findNavController().navigate(R.id.action_newsDetailFragment_to_newsFragment)
        } else {
            findNavController().navigate(R.id.action_newsDetailFragment_to_favoriteNewsFragment)
        }
    }

    fun goToNextScreen() {
        findNavController().navigate(
            R.id.action_newsDetailFragment_to_newsSourceFragment,
            Bundle().apply { putString("newsUrl", newsUrl) })
    }
}