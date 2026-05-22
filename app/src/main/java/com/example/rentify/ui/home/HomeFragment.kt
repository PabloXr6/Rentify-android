package com.example.rentify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // Setup ViewBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadDummyData()
    }

    private fun setupRecyclerView() {
        vehicleAdapter = VehicleAdapter()
        binding.rvVehicles.apply {
            // Menggunakan LinearLayoutManager agar list memanjang ke bawah sesuai desain Figma
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vehicleAdapter
        }
    }

    private fun loadDummyData() {
        // Memasukkan data statis (dummy) untuk mengetes UI
        val dummyList = listOf(
            DummyCar(1, "Toyota Avanza", "Rp.400.000 /Day", "4.7"),
            DummyCar(2, "Toyota Kijang Innova", "Rp.450.000 /Day", "4.9"),
            DummyCar(3, "Toyota Yaris", "Rp.350.000 /Day", "4.8"),
            DummyCar(4, "Toyota Fortuner", "Rp.1.200.000 /Day", "4.9")
        )

        // Memasukkan data ke adapter
        vehicleAdapter.submitList(dummyList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Mencegah memory leak saat fragment dihancurkan
        _binding = null
    }
}