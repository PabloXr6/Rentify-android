package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.R
import com.example.rentify.databinding.FragmentAdminDashboardBinding

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackDashboard.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cardAddVehicle.setOnClickListener {
            findNavController().navigate(R.id.adminAddVehicleFragment)
        }

        // Tombol Showroom (sudah dirapikan dari yang sebelumnya double)
        binding.cardManageShowroom.setOnClickListener {
            findNavController().navigate(R.id.adminManageShowroomsFragment)
        }

        binding.btnManageVehicles.setOnClickListener {
            findNavController().navigate(R.id.adminManageVehiclesFragment)
        }

        binding.cardAddArticles.setOnClickListener {
            findNavController().navigate(R.id.adminAddArticleFragment)

            binding.cardManageArticles.setOnClickListener {
                findNavController().navigate(R.id.adminManageArticleFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}