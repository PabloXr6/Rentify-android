package com.example.rentify.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.databinding.FragmentDetailVehicleBinding

class DetailVehicleFragment : Fragment() {

    private var _binding: FragmentDetailVehicleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailVehicleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Logika Tombol Back (Kembali ke halaman sebelumnya)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 2. Logika Tombol Booking Now (Direct ke WhatsApp)
        binding.btnBookingNow.setOnClickListener {
            directToWhatsApp()
        }
    }

    private fun directToWhatsApp() {
        // Nomor WhatsApp tujuan (Format internasional tanpa tanda '+')
        // Ganti dengan nomor asli nanti
        val phoneNumber = "6281234567890"

        // Pesan otomatis yang akan terketik di WhatsApp
        val carName = binding.tvCarName.text.toString()
        val message = "Halo Lombok Car Transport, saya tertarik menyewa mobil $carName dari aplikasi Rentify. Apakah unitnya tersedia?"

        // Membuat format URL API WhatsApp
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Aplikasi WhatsApp tidak ditemukan di perangkat ini", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}