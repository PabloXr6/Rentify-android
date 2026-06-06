package com.example.rentify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.data.repository.VehicleRepository
import com.example.rentify.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehicleAdapter: VehicleAdapter

    // Sekarang kita panggil sang Manajer Gudang (Repository)
    private lateinit var repository: VehicleRepository

    private var allVehicles: List<Vehicle> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Repository
        repository = VehicleRepository()

        setupRecyclerView()

        // Tarik data menggunakan Repository
        fetchVehicles()

        // Logika Tombol Filter
        binding.btnCars.setOnClickListener {
            filterData("Car")
        }

        binding.btnMotorcycles.setOnClickListener {
            filterData("Motorcycle")
        }
    }

    private fun setupRecyclerView() {
        vehicleAdapter = VehicleAdapter()
        binding.rvVehicles.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = vehicleAdapter
        }
    }

    private fun fetchVehicles() {
        // Menyuruh Repository mengambil data dari awan
        repository.getVehiclesFromCloud(
            onSuccess = { vehicleList ->
                allVehicles = vehicleList
                filterData("Car") // Filter default
            },
            onFailure = { exception ->
                Toast.makeText(requireContext(), "Gagal mengambil data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun filterData(category: String) {
        val filteredList = allVehicles.filter { it.category == category }
        vehicleAdapter.submitList(filteredList)
        binding.tvRecommendationTitle.text = "$category Recommendation"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}