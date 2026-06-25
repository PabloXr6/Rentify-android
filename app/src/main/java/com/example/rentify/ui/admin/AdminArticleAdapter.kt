package com.example.rentify.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentify.data.remote.Article
import com.example.rentify.databinding.ItemManageArticleBinding

class AdminArticleAdapter(
    private val onEditClick: (Article) -> Unit,
    private val onDeleteClick: (Article) -> Unit
) : ListAdapter<Article, AdminArticleAdapter.AdminArticleViewHolder>(DiffCallback()) {

    inner class AdminArticleViewHolder(private val binding: ItemManageArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvTitle.text = article.title
            binding.tvCategory.text = article.category
            binding.tvDate.text = article.date

            Glide.with(itemView.context)
                .load(article.imageUrl)
                .centerCrop()
                .into(binding.ivArticleImage)

            binding.btnEdit.setOnClickListener {
                onEditClick(article)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminArticleViewHolder {
        val binding = ItemManageArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            // Akan lebih baik jika dibandingkan dengan ID, tapi jika belum ada, pakai judul tidak masalah
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}