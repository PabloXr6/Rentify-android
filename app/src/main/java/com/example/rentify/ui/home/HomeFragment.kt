package com.example.rentify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.data.pref.UserPreferences
import com.example.rentify.databinding.FragmentHomeBinding
import com.example.rentify.ui.ViewModelFactory
import com.example.rentify.ui.favorites.FavoriteViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: HomeViewModel by viewModels { factory }
        favoriteViewModel = androidx.lifecycle.ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        // Set username dari preferences
        val userPrefs = UserPreferences(requireContext())
        binding.tvUserName.text = userPrefs.getUsername() ?: "Guest"

        setupRecyclerView()

        // Observers for favorites list
        favoriteViewModel.favoritesList.observe(viewLifecycleOwner) { favorites ->
            val favoriteIds = favorites.map { it.id }.toSet()
            vehicleAdapter.setFavoriteIds(favoriteIds)
        }
        favoriteViewModel.getAllFavorites()

        // Mengamati status loading & data dari ViewModel
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Loading -> {
                    // Loading state
                }
                is HomeState.Success -> {
                    // Success state
                }
                is HomeState.Error -> {
                    Toast.makeText(requireContext(), "Gagal mengambil data: ${state.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.filteredVehicles.observe(viewLifecycleOwner) { vehicles ->
            vehicleAdapter.submitList(vehicles)
        }

        viewModel.currentCategory.observe(viewLifecycleOwner) { category ->
            binding.tvRecommendationTitle.text = "$category Recommendation"
        }

        // Ambil data kendaraan
        viewModel.fetchVehicles()

        // Logika Tombol Filter
        binding.btnCars.setOnClickListener {
            viewModel.filterVehicles("Car")
        }

        binding.btnMotorcycles.setOnClickListener {
            viewModel.filterVehicles("Motorcycle")
        }
    }

    private fun setupRecyclerView() {
        vehicleAdapter = VehicleAdapter()
        vehicleAdapter.onFavoriteClick = { vehicle, isFav ->
            val entity = com.example.rentify.data.local.VehicleEntity(
                id = vehicle.id,
                name = vehicle.name,
                price = vehicle.price,
                rating = vehicle.rating,
                imageUrl = vehicle.imageUrl
            )
            if (isFav) {
                favoriteViewModel.removeFavorite(entity)
                Toast.makeText(requireContext(), "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.addFavorite(entity)
                Toast.makeText(requireContext(), "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvVehicles.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = vehicleAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        if (::favoriteViewModel.isInitialized) {
            favoriteViewModel.getAllFavorites()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}