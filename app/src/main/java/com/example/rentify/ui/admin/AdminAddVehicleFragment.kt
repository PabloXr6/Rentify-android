package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.databinding.FragmentAdminAddVehicleBinding
import com.example.rentify.ui.ViewModelFactory

class AdminAddVehicleFragment : Fragment() {

    private var _binding: FragmentAdminAddVehicleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddVehicleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackAddVehicle.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveVehicle.setOnClickListener {
            val name = binding.etVehicleName.text.toString().trim()
            val price = binding.etVehiclePrice.text.toString().trim()
            val category = binding.etVehicleCategory.text.toString().trim()
            val transmission = binding.etVehicleTransmission.text.toString().trim()
            val seats = binding.etVehicleSeats.text.toString().trim()
            val imageUrl = binding.etVehicleImageUrl.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || category.isEmpty()) {
                Toast.makeText(requireContext(), "Nama, harga, dan kategori wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val vehicle = Vehicle(
                name = name,
                price = price,
                category = category,
                transmission = transmission,
                seats = seats,
                imageUrl = imageUrl
            )

            viewModel.addVehicle(vehicle)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdminState.Loading -> binding.btnSaveVehicle.isEnabled = false
                is AdminState.Success -> {
                    Toast.makeText(requireContext(), "Kendaraan berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                    findNavController().navigateUp()
                }
                is AdminState.Error -> {
                    binding.btnSaveVehicle.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                else -> binding.btnSaveVehicle.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
