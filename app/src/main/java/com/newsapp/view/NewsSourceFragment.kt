package com.newsapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.newsapp.databinding.FragmentNewsSourceBinding


class NewsSourceFragment : Fragment() {

    private lateinit var binding: FragmentNewsSourceBinding
    private var safeBrowsingInitialized: Boolean = true

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsSourceBinding.inflate(inflater, container, false)
        safeBrowsingInitialized = false
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        // haberin url'sine gider
        binding.webview.loadUrl(requireArguments().getString("newsUrl").toString())
        return binding.root
    }
}



