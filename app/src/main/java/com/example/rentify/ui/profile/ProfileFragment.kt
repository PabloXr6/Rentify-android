package com.example.rentify.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.R
import com.example.rentify.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigasi ke Edit Profile
        binding.tvMenuEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

        // Navigasi ke Settings
        binding.tvMenuSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logout ditekan!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}