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

    inner class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Vehicle) {
            binding.tvCarName.text = car.name
            binding.tvCarPrice.text = car.price
            binding.tvRating.text = car.rating
            binding.tvTransmission.text = car.transmission
            binding.tvSeats.text = car.seats

                    Glide.with(binding.root.context)
                        .load(car.imageUrl)
                        .centerCrop()
                        .into(binding.ivCar)

            binding.root.setOnClickListener {
                it.findNavController().navigate(R.id.detailVehicleFragment)
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