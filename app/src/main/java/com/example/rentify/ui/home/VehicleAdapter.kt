package com.example.rentify.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rentify.databinding.ItemVehicleBinding
import com.example.rentify.R

// Data class dummy sementara untuk menampilkan list
data class DummyCar(val id: Int, val name: String, val price: String, val rating: String)

class VehicleAdapter : ListAdapter<DummyCar, VehicleAdapter.VehicleViewHolder>(DiffCallback()) {

    inner class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: DummyCar) {
            binding.tvCarName.text = car.name
            binding.tvCarPrice.text = car.price
            binding.tvRating.text = car.rating

            binding.root.setOnClickListener {
                it.findNavController().navigate(R.id.detailVehicleFragment)
            }

            // Nantinya logika klik tombol favorit dan klik item bisa ditambahkan di sini
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DummyCar>() {
        override fun areItemsTheSame(oldItem: DummyCar, newItem: DummyCar): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DummyCar, newItem: DummyCar): Boolean {
            return oldItem == newItem
        }
    }
}