package com.onyx.rssreader.fragments

import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.onyx.rssreader.MainActivity
import com.onyx.rssreader.R
import com.onyx.rssreader.databinding.FragmentArticleBinding

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
    }

    private fun setUpWebView() {
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webChromeClient = WebChromeClient()
        var mainContent = args.article?.content
        if (mainContent.isNullOrEmpty()) {
            mainContent = args.article?.description
        }
        binding.webView.loadDataWithBaseURL(
            null,
            "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n" +
                    "<style>iframe{ height: auto; width: auto;}</style>\n" +
                    "<h1>" + args.article?.title + "</h1>" +
                    "<h6 align=\"right\">" + args.article?.pubDate + "</h6>" +
                    "<img src=\"" + args.article?.image + "\" alt=\"Image Preview\">" +
                    mainContent, null, "utf-8", null
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.article_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                view?.findNavController()?.navigate(R.id.action_articleFragment_to_homeFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}