package com.example.rentify.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentify.data.remote.Article
import com.example.rentify.databinding.ItemArticleBinding

class ArticleAdapter : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(DiffCallback()) {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvArticleTitle.text = article.title
            binding.tvCategory.text = article.category
            binding.tvArticleDate.text = "${article.date} • ${article.readTime}"

            Glide.with(itemView.context)
                .load(article.imageUrl)
                .centerCrop()
                .into(binding.  ivArticle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}