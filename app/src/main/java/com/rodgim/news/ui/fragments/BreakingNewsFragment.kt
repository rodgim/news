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
import androidx.navigation.fragment.findNavController
import com.rodgim.news.R
import com.rodgim.news.databinding.FragmentBreakingNewsBinding
import com.rodgim.news.domain.models.Article
import com.rodgim.news.ui.UIConstants
import com.rodgim.news.ui.adapters.NewsAdapter
import com.rodgim.news.ui.extensions.lastVisibleEvents
import com.rodgim.news.ui.extensions.showToast
import com.rodgim.news.ui.viewmodels.BreakingNewsUiModel
import com.rodgim.news.ui.viewmodels.BreakingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var newsAdapter: NewsAdapter
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
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
            newsAdapter = NewsAdapter { article ->
                goToArticle(article)
            }
            adapter = newsAdapter
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                binding.rvBreakingNews.lastVisibleEvents.collect {
                    viewModel.lastVisible.value = it
                }
            }
        }
    }

    private fun showLoading() {
        binding.paginationProgressBar.isVisible = true
    }

    private fun hideLoading() {
        binding.paginationProgressBar.isVisible = false
    }

    private fun goToArticle(article: Article) {
        val bundle = Bundle().apply {
            putParcelable(UIConstants.ARTICLE, article)
        }
        findNavController().navigate(
            R.id.action_breakingNewsFragment_to_articleFragment,
            bundle
        )
    }
}
