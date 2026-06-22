package com.example.rentify.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.databinding.ItemAdminVehicleBinding

class AdminVehicleAdapter(
    private val onEditClick: (Vehicle) -> Unit,
    private val onDeleteClick: (Vehicle) -> Unit
) : ListAdapter<Vehicle, AdminVehicleAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemAdminVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.tvCarName.text = vehicle.name
            binding.tvShowroomName.text = vehicle.showroomName.ifEmpty { "Showroom Tidak Diketahui" }
            binding.tvCarPrice.text = vehicle.price

            Glide.with(binding.root.context)
                .load(vehicle.imageUrl)
                .centerCrop()
                .into(binding.ivCar)

            binding.btnEdit.setOnClickListener { onEditClick(vehicle) }
            binding.btnDelete.setOnClickListener { onDeleteClick(vehicle) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle) = oldItem == newItem
    }
}