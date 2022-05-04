package com.newsapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.newsapp.R
import com.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // tablar arası geçişi sağlar
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    goToFavoriteNewsScreen()
                } else {
                    goToNewsScreen()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // tablayout ve toolbarı fragmentlara göre ayarlar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newsDetailFragment -> {
                    binding.tablayout.visibility = View.GONE
                }
                R.id.newsSourceFragment -> {
                    binding.tablayout.visibility = View.GONE
                }
                else -> {
                    binding.tablayout.visibility = View.VISIBLE
                }
            }
        }

        // dark modun varlığına göre bazı ayarlamalar yapar
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.relativeLayout.setBackgroundColor(getColor(R.color.white100))
                binding.tablayout.background = getDrawable(R.drawable.tablayout_bg)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.tablayout.background = getDrawable(R.drawable.tablayout_bg)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun goToFavoriteNewsScreen() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_newsFragment_to_favoriteNewsFragment)
    }

    fun goToNewsScreen() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_favoriteNewsFragment_to_newsFragment)
    }

}