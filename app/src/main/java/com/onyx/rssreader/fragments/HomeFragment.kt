package com.onyx.rssreader.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.onyx.rssreader.MainActivity
import com.onyx.rssreader.R
import com.onyx.rssreader.adapters.ArticlePreviewAdapter
import com.onyx.rssreader.databinding.FragmentHomeBinding
import com.onyx.rssreader.viewmodels.ArticleViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var articlePreviewAdapter: ArticlePreviewAdapter
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleViewModel = ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        articlePreviewAdapter = ArticlePreviewAdapter()

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = articlePreviewAdapter
        }

        activity?.let {
            articleViewModel.articles.observe(viewLifecycleOwner, { list ->
                articlePreviewAdapter.articles = list.toMutableList()
                articlePreviewAdapter.notifyDataSetChanged()
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.rss_url -> {
                getRSSFeedUrl()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getRSSFeedUrl() {
        val input = EditText(activity)
        AlertDialog.Builder(activity).apply {
            setTitle("Enter RSS Feed URL:")
            setView(input)
            setPositiveButton("Ok") { _,_ ->
                binding.recyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                articleViewModel.setUrl(input.text.toString().trim())
            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}