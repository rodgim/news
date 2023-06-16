package com.rodgim.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rodgim.news.R
import com.rodgim.news.databinding.FragmentSearchNewsBinding
import com.rodgim.news.domain.models.Article
import com.rodgim.news.ui.UIConstants
import com.rodgim.news.ui.adapters.NewsAdapter
import com.rodgim.news.ui.extensions.lastVisibleEvents
import com.rodgim.news.ui.extensions.showToast
import com.rodgim.news.ui.viewmodels.SearchNewsUiModel
import com.rodgim.news.ui.viewmodels.SearchNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment() {

    companion object {
        const val SEARCH_TIME_DELAY = 500L
    }

    private lateinit var binding: FragmentSearchNewsBinding
    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter { article ->
            goToArticle(article)
        }
    }
    private val viewModel: SearchNewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchEditText()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when(it) {
                        is SearchNewsUiModel.Error -> {
                            hideLoading()
                            showToast(it.message)
                        }
                        is SearchNewsUiModel.Load -> {
                            hideLoading()
                            val currentList = newsAdapter.currentList.toMutableList()
                            currentList.addAll(it.articles)
                            newsAdapter.submitList(currentList)
                        }
                        SearchNewsUiModel.Loading -> showLoading()
                    }
                }
            }
        }
    }

    private fun setupSearchEditText() {
        var job: Job? = null
        binding.etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (it.toString().isNotEmpty()) {
                        newsAdapter.submitList(emptyList())
                        viewModel.search.value = viewModel.search.value.copy(queryText = it.toString() ,page = 1)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchNews.apply {
            adapter = newsAdapter
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.rvSearchNews.lastVisibleEvents.collect {
                    viewModel.search.value = viewModel.search.value.copy(page = it)
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
            R.id.action_searchNewsFragment_to_articleFragment,
            bundle
        )
    }
}
