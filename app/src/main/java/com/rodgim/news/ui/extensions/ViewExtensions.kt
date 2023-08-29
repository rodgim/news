package com.rodgim.news.ui.extensions

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.rodgim.news.ui.UIConstants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

fun ImageView.loadImageFromUrl(url: String?) {
    url?.let {
        Glide
            .with(context)
            .load(it)
            .into(this)
    }
}

fun Fragment.showSnackbar(message: String) {
    view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
}

val RecyclerView.lastVisibleEvents: Flow<Int>
    get() = callbackFlow<Int> {
        val layoutManager = layoutManager as LinearLayoutManager

        val listener = object : OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val size = layoutManager.itemCount
                if (lastVisibleItemPosition >= (size - UIConstants.PAGE_THRESHOLD)) {
                    trySend(size / UIConstants.NEWS_PAGE_SIZE + 1)
                }
            }
        }
        addOnScrollListener(listener)
        awaitClose { removeOnScrollListener(listener) }
    }.conflate()

