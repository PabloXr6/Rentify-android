package com.example.rentify.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentify.R
import com.example.rentify.data.local.VehicleEntity
import com.example.rentify.databinding.ItemVehicleBinding

class FavoriteAdapter : ListAdapter<VehicleEntity, FavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {

    inner class FavoriteViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: VehicleEntity) {
            binding.tvCarName.text = car.name
            binding.tvCarPrice.text = car.price
            binding.tvRating.text = car.rating
            binding.tvTransmission.text = car.transmission
            binding.tvSeats.text = if (car.seats.contains("Seat", ignoreCase = true)) car.seats else "${car.seats} Seats"

            Glide.with(binding.root.context)
                .load(car.imageUrl)
                .fitCenter()
                .into(binding.ivCar)

            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("id", car.id)
                    putString("name", car.name)
                    putString("price", car.price)
                    putString("rating", car.rating)
                    putString("imageUrl", car.imageUrl)
                    putString("transmission", car.transmission)
                    putString("seats", car.seats)
                    putString("category", car.category)
                    putString("showroomId", car.showroomId)
                }
                it.findNavController().navigate(R.id.detailVehicleFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<VehicleEntity>() {
        override fun areItemsTheSame(oldItem: VehicleEntity, newItem: VehicleEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VehicleEntity, newItem: VehicleEntity): Boolean {
            return oldItem == newItem
        }
    }
}