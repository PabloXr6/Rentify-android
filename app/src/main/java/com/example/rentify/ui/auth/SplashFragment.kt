package com.example.rentify.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.R
import com.example.rentify.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set posisi awal logo di bawah layar (di luar pandangan)
        binding.tvLogoRentify.translationY = 1000f
        binding.tvLogoRentify.alpha = 0f

        // Jalankan Animasi: Geser ke atas (posisi 0) dan munculkan (alpha 1)
        binding.tvLogoRentify.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1500) // Durasi 1.5 detik
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()

        // Pindah ke halaman Login setelah 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.signInFragment)
        }, 3000)
    }
}