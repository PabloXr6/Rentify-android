package com.example.rentify.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Kembali ke Profile
        binding.btnBackSettings.setOnClickListener {
            findNavController().navigateUp()
        }

        // Contact Support -> Direct ke WhatsApp Admin Rentify
        binding.tvMenuSupport.setOnClickListener {
            val phoneNumber = "6283142646621" // Nomor ayu
            val message = "Halo Admin Rentify, saya butuh bantuan mengenai aplikasi."
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(requireContext(), "WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        // Terms and Conditions
        binding.tvMenuTerms.setOnClickListener {
            Toast.makeText(requireContext(), "Halaman Syarat & Ketentuan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}