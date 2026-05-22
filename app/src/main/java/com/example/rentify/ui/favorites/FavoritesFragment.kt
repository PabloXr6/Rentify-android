package com.example.rentify.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentFavoritesBinding
import com.example.rentify.ui.home.DummyCar
import com.example.rentify.ui.home.VehicleAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    // Memakai ulang adapter dari HomeFragment
    private lateinit var favoriteAdapter: VehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadDummyFavorites()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = VehicleAdapter()
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    private fun loadDummyFavorites() {
        // Simulasi: Mengambil 2 mobil saja seolah-olah ini adalah mobil yang disukai user
        val dummyList = listOf(
            DummyCar(1, "Toyota Avanza", "Rp.400.000 /Day", "4.7"),
            DummyCar(3, "Toyota Yaris", "Rp.350.000 /Day", "4.8")
        )

        favoriteAdapter.submitList(dummyList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}