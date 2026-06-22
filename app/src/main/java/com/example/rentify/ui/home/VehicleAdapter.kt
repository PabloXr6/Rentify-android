package com.example.rentify.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentify.R
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.databinding.ItemVehicleBinding

class VehicleAdapter : ListAdapter<Vehicle, VehicleAdapter.VehicleViewHolder>(DiffCallback()) {

    var onFavoriteClick: ((Vehicle, Boolean) -> Unit)? = null
    private var favoriteIds: Set<String> = emptySet()

    fun setFavoriteIds(ids: Set<String>) {
        favoriteIds = ids
        notifyDataSetChanged()
    }

    inner class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Vehicle) {
            binding.tvCarName.text = car.name
            binding.tvCarPrice.text = car.price
            binding.tvRating.text = car.rating
            binding.tvTransmission.text = car.transmission
            binding.tvSeats.text = if (car.seats.contains("Seat", ignoreCase = true)) car.seats else "${car.seats} Seats"

            Glide.with(binding.root.context)
                .load(car.imageUrl)
                .fitCenter() // Ubah dari .centerCrop() menjadi .fitCenter()
                .into(binding.ivCar)

            val isFav = favoriteIds.contains(car.id)
            if (isFav) {
                binding.ivFavorite.setColorFilter(android.graphics.Color.parseColor("#F44336"))
            } else {
                binding.ivFavorite.setColorFilter(android.graphics.Color.parseColor("#BDBDBD"))
            }

            binding.cvFavorite.setOnClickListener {
                onFavoriteClick?.invoke(car, isFav)
            }

            binding.root.setOnClickListener {
                val bundle = android.os.Bundle().apply {
                    putString("id", car.id)
                    putString("name", car.name)
                    putString("price", car.price)
                    putString("rating", car.rating)
                    putString("imageUrl", car.imageUrl)
                    putString("transmission", car.transmission)
                    putString("seats", car.seats)
                    putString("category", car.category)

                    // INI DIA YANG KETINGGALAN!
                    putString("showroomId", car.showroomId)
                }
                it.findNavController().navigate(R.id.detailVehicleFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem == newItem
        }
    }
}