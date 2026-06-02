package com.example.rentify.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rentify.databinding.ItemArticleBinding
import androidx.navigation.findNavController
import com.example.rentify.R

// Data class sementara untuk artikel
data class DummyArticle(val id: Int, val title: String, val category: String, val date: String)

class ArticleAdapter : ListAdapter<DummyArticle, ArticleAdapter.ArticleViewHolder>(DiffCallback()) {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: DummyArticle) {
            binding.tvArticleTitle.text = article.title
            binding.tvCategory.text = article.category
            binding.tvArticleDate.text = article.date

            binding.root.setOnClickListener {
                it.findNavController().navigate(R.id.detailExploreFragment)
            }

            // Logika klik artikel bisa ditambahkan di sini nantinya
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DummyArticle>() {
        override fun areItemsTheSame(oldItem: DummyArticle, newItem: DummyArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DummyArticle, newItem: DummyArticle): Boolean {
            return oldItem == newItem
        }
    }
}