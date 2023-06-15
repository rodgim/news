package com.rodgim.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodgim.news.databinding.ItemArticlePreviewBinding
import com.rodgim.news.domain.models.Article
import com.rodgim.news.ui.extensions.loadImageFromUrl

class NewsAdapter(
    private val onItemClickListener: (Article) -> Unit
) : ListAdapter<Article, NewsAdapter.ViewHolder>(NewsArticleDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemArticlePreviewBinding.inflate(layoutInflater, parent, false),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val view: ItemArticlePreviewBinding,
        private val onItemClickListener: (Article) -> Unit
    ): RecyclerView.ViewHolder(view.root) {

        fun bind(article: Article) {
            with(view) {
                ivArticleImage.loadImageFromUrl(article.urlToImage)
                tvSource.text = article.source.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt
                root.setOnClickListener {
                    onItemClickListener(article)
                }
            }
        }
    }
}

class NewsArticleDiffUtil(): DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
