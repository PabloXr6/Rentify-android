package com.example.rentify.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.R
import com.example.rentify.databinding.FragmentSplashBinding
import com.example.rentify.ui.ViewModelFactory

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: AuthViewModel by viewModels { factory }

        // Animasi Logo Rentify
        binding.tvLogoRentify.translationY = 1000f
        binding.tvLogoRentify.alpha = 0f

        binding.tvLogoRentify.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1500)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()

        // Tunggu 3 detik, lalu cek status Login
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.checkLoginSession()) {
                // Jika user sudah login sebelumnya, langsung tembak ke Home
                findNavController().navigate(R.id.homeFragment)
            } else {
                // Jika belum login, arahkan ke halaman Sign In
                findNavController().navigate(R.id.signInFragment)
            }
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}