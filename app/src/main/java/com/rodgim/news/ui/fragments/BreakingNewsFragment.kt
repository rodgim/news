package com.rodgim.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rodgim.news.databinding.FragmentBreakingNewsBinding
import com.rodgim.news.ui.adapters.NewsAdapter
import com.rodgim.news.ui.extensions.showToast
import com.rodgim.news.ui.viewmodels.BreakingNewsUiModel
import com.rodgim.news.ui.viewmodels.BreakingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter { article ->

        }
    }
    private val viewModel: BreakingNewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when(it) {
                        is BreakingNewsUiModel.Error -> {
                            hideLoading()
                            showToast(it.message)
                        }
                        is BreakingNewsUiModel.Load -> {
                            hideLoading()
                            newsAdapter.submitList(it.articles)
                        }
                        BreakingNewsUiModel.Loading -> showLoading()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
        }
    }

    private fun showLoading() {
        binding.paginationProgressBar.isVisible = true
    }

    private fun hideLoading() {
        binding.paginationProgressBar.isVisible = false
    }
}
