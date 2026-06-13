package com.example.rentify.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.R
import com.example.rentify.data.pref.UserPreferences
import com.example.rentify.databinding.FragmentProfileBinding
import com.example.rentify.ui.ViewModelFactory
import com.example.rentify.ui.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

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

        // Setup ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: AuthViewModel by viewModels { factory }

        // Tampilkan nama dari UserPreferences & email dari Firebase Auth
        val userPrefs = UserPreferences(requireContext())
        val currentUser = FirebaseAuth.getInstance().currentUser
        val loggedInEmail = userPrefs.getEmail() ?: currentUser?.email ?: ""

        binding.tvProfileName.text = userPrefs.getUsername() ?: currentUser?.displayName ?: "Pengguna"
        binding.tvProfileEmail.text = if (loggedInEmail.isNotEmpty()) loggedInEmail else "-"

        // Tampilkan tombol Admin Panel jika email adalah admin
        val adminEmail = "admin@rentify.com" // ganti sesuai email admin
        if (loggedInEmail == adminEmail) {
            binding.tvMenuAdminPanel.visibility = View.VISIBLE
            binding.dividerAdmin.visibility = View.VISIBLE
        }

        // Navigasi ke Admin Panel
        binding.tvMenuAdminPanel.setOnClickListener {
            findNavController().navigate(R.id.adminDashboardFragment)
        }

        // Navigasi ke Edit Profile
        binding.tvMenuEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

        // Navigasi ke Settings
        binding.tvMenuSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        // Tombol Logout — fungsional: hapus sesi lalu ke SignIn
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            // Navigasi ke SignIn, bersihkan semua back stack
            findNavController().navigate(
                R.id.signInFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph_main, true)
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}