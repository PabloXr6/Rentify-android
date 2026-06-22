package com.example.rentify.ui.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.R
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.databinding.FragmentAdminManageVehiclesBinding
import com.example.rentify.ui.ViewModelFactory

class AdminManageVehiclesFragment : Fragment() {

    private var _binding: FragmentAdminManageVehiclesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: AdminVehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageVehiclesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.loadAllVehicles()

        viewModel.vehicles.observe(viewLifecycleOwner) { vehicles ->
            adapter.submitList(vehicles)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdminState.Success -> {
                    viewModel.loadAllVehicles()
                    viewModel.resetState()
                }
                is AdminState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                else -> {}
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = AdminVehicleAdapter(
            onEditClick = { vehicle ->
                val bundle = Bundle().apply {
                    putString("id", vehicle.id)
                    putString("name", vehicle.name)
                    putString("price", vehicle.price)
                    putString("category", vehicle.category)
                    putString("transmission", vehicle.transmission)
                    putString("seats", vehicle.seats)
                    putString("imageUrl", vehicle.imageUrl)
                    putString("rating", vehicle.rating)
                    putString("showroomId", vehicle.showroomId)
                    putString("showroomName", vehicle.showroomName)
                }
                findNavController().navigate(R.id.adminAddVehicleFragment, bundle)
            },
            onDeleteClick = { vehicle ->
                showDeleteConfirmation(vehicle)
            }
        )

        binding.rvAdminVehicles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@AdminManageVehiclesFragment.adapter
        }
    }

    private fun showDeleteConfirmation(vehicle: Vehicle) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Kendaraan")
            .setMessage("Apakah Anda yakin ingin menghapus ${vehicle.name} secara permanen?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteVehicle(vehicle.id)
                Toast.makeText(requireContext(), "Menghapus...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}