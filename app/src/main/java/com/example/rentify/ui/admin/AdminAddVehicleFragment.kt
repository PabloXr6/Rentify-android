package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.data.remote.Showroom
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.databinding.FragmentAdminAddVehicleBinding
import com.example.rentify.ui.ViewModelFactory

class AdminAddVehicleFragment : Fragment() {

    private var _binding: FragmentAdminAddVehicleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var listShowrooms: List<Showroom> = emptyList()
    private var selectedShowroomId = ""
    private var selectedShowroomName = ""

    // Variabel penanda mode Edit
    private var isEditMode = false
    private var vehicleId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddVehicleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupStaticDropdowns()

        arguments?.let {
            if (it.containsKey("id")) {
                isEditMode = true
                vehicleId = it.getString("id") ?: ""

                // Tempel data lama ke form input
                binding.etVehicleName.setText(it.getString("name"))
                binding.etVehiclePrice.setText(it.getString("price")?.replace("Rp ", "")?.replace(" / Hari", ""))
                binding.acVehicleCategory.setText(it.getString("category"), false)
                binding.acVehicleTransmission.setText(it.getString("transmission"), false)
                binding.etVehicleSeats.setText(it.getString("seats"))
                binding.etVehicleImageUrl.setText(it.getString("imageUrl"))
                binding.etVehicleRating.setText(it.getString("rating"))

                selectedShowroomId = it.getString("showroomId") ?: ""
                selectedShowroomName = it.getString("showroomName") ?: ""
                binding.acVehicleShowroom.setText(selectedShowroomName, false)

                // Ubah teks judul tombol
                binding.btnSaveVehicle.text = "Perbarui Kendaraan"
            }
        }

        viewModel.loadShowrooms()
        viewModel.showrooms.observe(viewLifecycleOwner) { showrooms ->
            if (showrooms != null) {
                listShowrooms = showrooms
                val showroomNames = showrooms.map { it.name }.toTypedArray()
                val showroomAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, showroomNames)
                binding.acVehicleShowroom.setAdapter(showroomAdapter)
            }
        }

        binding.acVehicleShowroom.setOnItemClickListener { _, _, position, _ ->
            val clickedShowroom = listShowrooms[position]
            selectedShowroomId = clickedShowroom.id
            selectedShowroomName = clickedShowroom.name
        }

        binding.btnBackAddVehicle.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveVehicle.setOnClickListener {
            val name = binding.etVehicleName.text.toString().trim()
            val price = binding.etVehiclePrice.text.toString().trim()
            val category = binding.acVehicleCategory.text.toString().trim()
            val transmission = binding.acVehicleTransmission.text.toString().trim()
            val seats = binding.etVehicleSeats.text.toString().trim()
            val imageUrl = binding.etVehicleImageUrl.text.toString().trim()
            val rating = binding.etVehicleRating.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || category.isEmpty() || rating.isEmpty() || selectedShowroomId.isEmpty()) {
                Toast.makeText(requireContext(), "Semua kolom utama wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val vehicle = Vehicle(
                id = vehicleId,
                name = name,
                price = "Rp $price / Hari",
                category = category,
                transmission = transmission,
                seats = seats,
                imageUrl = imageUrl,
                rating = rating,
                showroomId = selectedShowroomId,
                showroomName = selectedShowroomName
            )

            // Eksekusi berdasarkan mode aktif
            if (isEditMode) {
                viewModel.updateVehicle(vehicle)
            } else {
                viewModel.addVehicle(vehicle)
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdminState.Loading -> {
                    binding.btnSaveVehicle.isEnabled = false
                    binding.btnSaveVehicle.text = "Menyimpan..."
                }
                is AdminState.Success -> {
                    val msg = if (isEditMode) "Kendaraan berhasil diperbarui!" else "Kendaraan berhasil ditambahkan!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                    findNavController().navigateUp()
                }
                is AdminState.Error -> {
                    binding.btnSaveVehicle.isEnabled = true
                    binding.btnSaveVehicle.text = if (isEditMode) "Perbarui Kendaraan" else "Simpan Kendaraan"
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                else -> {
                    binding.btnSaveVehicle.isEnabled = true
                }
            }
        }
    }

    private fun setupStaticDropdowns() {
        val categories = arrayOf("Car", "Motorcycle")
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.acVehicleCategory.setAdapter(categoryAdapter)

        val transmissions = arrayOf("Automatic", "Manual")
        val transmissionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, transmissions)
        binding.acVehicleTransmission.setAdapter(transmissionAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}