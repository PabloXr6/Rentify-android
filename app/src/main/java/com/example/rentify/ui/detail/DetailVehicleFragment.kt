package com.example.rentify.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.rentify.R
import com.example.rentify.data.local.VehicleEntity
import com.example.rentify.databinding.FragmentDetailVehicleBinding
import com.example.rentify.ui.ViewModelFactory
import com.example.rentify.ui.admin.AdminViewModel
import com.example.rentify.ui.favorites.FavoriteViewModel

class DetailVehicleFragment : Fragment() {

    private var _binding: FragmentDetailVehicleBinding? = null
    private val binding get() = _binding!!

    private var showroomPhone: String = "6283142646621"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailVehicleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val favoriteViewModel: FavoriteViewModel by viewModels { factory }

        val adminViewModel: AdminViewModel by viewModels { factory }

        val id = arguments?.getString("id") ?: ""
        val name = arguments?.getString("name") ?: ""
        val price = arguments?.getString("price") ?: ""
        val rating = arguments?.getString("rating") ?: ""
        val imageUrl = arguments?.getString("imageUrl") ?: ""
        val transmission = arguments?.getString("transmission") ?: ""
        val seats = arguments?.getString("seats") ?: ""
        val showroomId = arguments?.getString("showroomId") ?: ""
        val category = arguments?.getString("category") ?: ""

        binding.tvCarName.text = name
        binding.tvRating.text = rating
        binding.tvPriceValue.text = price.substringBefore(" /")

        binding.tvDetailCategory.text = category
        binding.tvDetailTransmission.text = transmission
        binding.tvDetailSeats.text = if (seats.contains("Seat")) seats else "$seats Seats"

        if (category.equals("Motorcycle", ignoreCase = true)) {
            binding.tvHeaderTitle.text = "Motorcycle Details"
            binding.ivDetailCategoryIcon.setImageResource(R.drawable.ic_motorcycle)
        } else {
            binding.tvHeaderTitle.text = "Car Details"
            binding.ivDetailCategoryIcon.setImageResource(R.drawable.ic_car) 
        }

        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.ivCarDetail)

        // 1. Ambil data dari Firebase berdasarkan ID showroom mobil ini
        if (showroomId.isNotEmpty()) {
            adminViewModel.loadShowroomById(showroomId)
        } else {
            // Jika ID kosong, coba ambil data showroom utama sebagai fallback
            adminViewModel.loadShowrooms()
        }

        adminViewModel.singleShowroom.observe(viewLifecycleOwner) { showroom ->
            if (showroom != null && showroom.name.isNotEmpty()) {
                showroomPhone = showroom.phone
                binding.tvPartnerName.text = showroom.name
                binding.tvAboutDesc.text = showroom.description
            }
        }

        // Jika loadShowrooms (list) terpanggil, ambil item pertama
        adminViewModel.showrooms.observe(viewLifecycleOwner) { showrooms ->
            if (showroomId.isEmpty() && showrooms.isNotEmpty()) {
                val showroom = showrooms[0]
                showroomPhone = showroom.phone
                binding.tvPartnerName.text = showroom.name
                binding.tvAboutDesc.text = showroom.description
            }
        }

        favoriteViewModel.checkIsFavorite(id)

        var isFav = false
        favoriteViewModel.isFavorite.observe(viewLifecycleOwner) { favorite ->
            isFav = favorite
            if (favorite) {
                binding.btnFavorite.setColorFilter(android.graphics.Color.parseColor("#F44336"))
            } else {
                binding.btnFavorite.setColorFilter(android.graphics.Color.parseColor("#000000"))
            }
        }

        binding.btnFavorite.setOnClickListener {
            if (id.isEmpty()) {
                Toast.makeText(requireContext(), "Data kendaraan tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Masukkan SEMUA data ke dalam entity, jangan ada yang tertinggal!
            val entity = VehicleEntity(
                id = id,
                name = name,
                price = price,
                rating = rating,
                imageUrl = imageUrl,
                transmission = transmission,
                seats = seats,
                category = category,         // (Pastikan kamu sudah membuat val category = arguments?.getString("category") ?: "" di bagian atas ya)
                showroomId = showroomId
            )

            if (isFav) {
                favoriteViewModel.removeFavorite(entity)
                Toast.makeText(requireContext(), "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.addFavorite(entity)
                Toast.makeText(requireContext(), "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnBookingNow.setOnClickListener {
            directToWhatsApp(name, showroomPhone)
        }
    }

    private fun directToWhatsApp(carName: String, phoneNumber: String) {
        val message = "Halo, saya tertarik menyewa kendaraan $carName dari aplikasi Rentify. Apakah unitnya tersedia?"
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