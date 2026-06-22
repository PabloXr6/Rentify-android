package com.example.rentify.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rentify.data.remote.Showroom
import com.example.rentify.databinding.ItemAdminShowroomBinding

class AdminShowroomAdapter(
    private val onEditClick: (Showroom) -> Unit,
    private val onDeleteClick: (Showroom) -> Unit
) : ListAdapter<Showroom, AdminShowroomAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemAdminShowroomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(showroom: Showroom) {
            binding.tvShowroomName.text = showroom.name
            binding.tvShowroomPhone.text = "WA: ${showroom.phone}"

            binding.btnEdit.setOnClickListener { onEditClick(showroom) }
            binding.btnDelete.setOnClickListener { onDeleteClick(showroom) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminShowroomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Showroom>() {
        override fun areItemsTheSame(oldItem: Showroom, newItem: Showroom) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Showroom, newItem: Showroom) = oldItem == newItem
    }
}