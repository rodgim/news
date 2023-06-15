package com.rodgim.news.ui.extensions

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String?) {
    url?.let {
        Glide
            .with(context)
            .load(it)
            .into(this)
    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
