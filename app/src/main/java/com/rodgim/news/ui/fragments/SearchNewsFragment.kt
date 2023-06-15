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
import com.rodgim.news.databinding.FragmentSearchNewsBinding
import com.rodgim.news.ui.adapters.NewsAdapter
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
                            newsAdapter.submitList(it.articles)
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
                    viewModel.search(it.toString())
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchNews.apply {
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
