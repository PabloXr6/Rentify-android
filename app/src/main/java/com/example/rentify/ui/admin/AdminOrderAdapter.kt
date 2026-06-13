package com.example.rentify.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rentify.data.remote.Order
import com.example.rentify.databinding.ItemOrderBinding

class AdminOrderAdapter(
    private val onConfirm: (Order) -> Unit,
    private val onCancel: (Order) -> Unit
) : ListAdapter<Order, AdminOrderAdapter.OrderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvOrderVehicleName.text = order.vehicleName
            binding.tvOrderUserName.text = "Pemesan: ${order.userName}"
            binding.tvOrderDate.text = "${order.rentDate} — ${order.returnDate}"
            binding.tvOrderPrice.text = order.totalPrice
            binding.tvOrderStatus.text = order.status.replaceFirstChar { it.uppercase() }

            // Warna status
            val statusColor = when (order.status) {
                "confirmed" -> "#4CAF50"
                "completed" -> "#2196F3"
                "cancelled" -> "#F44336"
                else -> "#FF9800" // pending
            }
            binding.tvOrderStatus.backgroundTintList =
                android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor(statusColor))

            binding.btnOrderConfirm.setOnClickListener { onConfirm(order) }
            binding.btnOrderCancel.setOnClickListener { onCancel(order) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
        }
    }
}
