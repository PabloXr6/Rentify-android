package com.example.rentify.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.databinding.FragmentFavoritesBinding
import com.example.rentify.ui.home.VehicleAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

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

        favoriteAdapter = VehicleAdapter()
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }

        // List sementara menggunakan format Vehicle yang baru
        // Nantinya data ini akan ditarik dari Room Database buatan temanmu
        val dummyFavorites = listOf(
            Vehicle(id = "1", name = "Toyota Avanza", price = "Rp 400.000 / Hari", rating = "4.7"),
            Vehicle(id = "2", name = "Honda Brio", price = "Rp 350.000 / Hari", rating = "4.8")
        )

        favoriteAdapter.submitList(dummyFavorites)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}