package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentAdminOrdersBinding
import com.example.rentify.ui.ViewModelFactory

class AdminOrdersFragment : Fragment() {

    private var _binding: FragmentAdminOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: AdminOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackOrders.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = AdminOrderAdapter(
            onConfirm = { order ->
                viewModel.updateOrderStatus(order.id, "confirmed")
                Toast.makeText(requireContext(), "Pesanan ${order.vehicleName} dikonfirmasi", Toast.LENGTH_SHORT).show()
            },
            onCancel = { order ->
                viewModel.updateOrderStatus(order.id, "cancelled")
                Toast.makeText(requireContext(), "Pesanan ${order.vehicleName} dibatalkan", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter

        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            adapter.submitList(orders)
        }

        viewModel.loadAllOrders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
