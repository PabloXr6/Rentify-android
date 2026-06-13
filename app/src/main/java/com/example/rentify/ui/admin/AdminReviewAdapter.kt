package com.example.rentify.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rentify.data.remote.Review
import com.example.rentify.databinding.ItemReviewBinding

class AdminReviewAdapter(
    private val onDelete: (Review) -> Unit
) : ListAdapter<Review, AdminReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.tvReviewUserName.text = review.userName
            binding.tvReviewVehicleName.text = review.vehicleName
            binding.tvReviewComment.text = review.comment
            binding.tvReviewDate.text = review.date
            binding.tvReviewRating.text = "⭐ ${review.rating}"

            binding.btnDeleteReview.setOnClickListener { onDelete(review) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
        }
    }
}
