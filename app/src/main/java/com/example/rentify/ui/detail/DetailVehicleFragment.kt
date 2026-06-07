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
import com.example.rentify.ui.favorites.FavoriteViewModel

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

        // Menerima data dari Bundle
        val id = arguments?.getString("id") ?: ""
        val name = arguments?.getString("name") ?: ""
        val price = arguments?.getString("price") ?: ""
        val rating = arguments?.getString("rating") ?: ""
        val imageUrl = arguments?.getString("imageUrl") ?: ""

        // Tampilkan data ke UI
        binding.tvCarName.text = name
        binding.tvRating.text = rating
        binding.tvPriceValue.text = price.substringBefore(" /") 

        Glide.with(this)
            .load(imageUrl)
            .centerInside()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(binding.ivCarDetail)

        // Setup ViewModel untuk Favorites (Room)
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: FavoriteViewModel by viewModels { factory }

        // Cek status favorite
        viewModel.checkIsFavorite(id)

        var isFav = false
        viewModel.isFavorite.observe(viewLifecycleOwner) { favorite ->
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
            val entity = VehicleEntity(
                id = id,
                name = name,
                price = price,
                rating = rating,
                imageUrl = imageUrl
            )
            if (isFav) {
                viewModel.removeFavorite(entity)
                Toast.makeText(requireContext(), "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addFavorite(entity)
                Toast.makeText(requireContext(), "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
            }
        }

        // 1. Logika Tombol Back
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 2. Logika Tombol Booking Now (Direct ke WhatsApp)
        binding.btnBookingNow.setOnClickListener {
            directToWhatsApp(name)
        }
    }

    private fun directToWhatsApp(carName: String) {
        val phoneNumber = "6283142646621"
        val message = "Halo Lombok Car Transport, saya tertarik menyewa kendaraan $carName dari aplikasi Rentify. Apakah unitnya tersedia?"
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