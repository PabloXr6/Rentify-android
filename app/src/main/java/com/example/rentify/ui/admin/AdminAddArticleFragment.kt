package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.data.remote.Article
import com.example.rentify.databinding.FragmentAdminAddArticleBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddArticleFragment : Fragment() {

    private var _binding: FragmentAdminAddArticleBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- INI KODE BARU UNTUK TOMBOL BACK ---
        binding.btnBackAddArticle.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSubmitArticle.setOnClickListener {
            // Mengambil teks dari semua input, termasuk etImageUrl yang baru
            val title = binding.etTitle.text.toString()
            val category = binding.etCategory.text.toString()
            val date = binding.etDate.text.toString()
            val imageUrl = binding.etImageUrl.text.toString()

            // Validasi sederhana agar tidak ada data kosong
            if (title.isNotEmpty() && category.isNotEmpty() && imageUrl.isNotEmpty()) {
                val newArticle = Article(
                    title = title,
                    category = category,
                    date = date,
                    readTime = "5 min read",
                    imageUrl = imageUrl
                )

                db.collection("explore_articles")
                    .add(newArticle)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Artikel berhasil ditambah!", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Mohon isi semua field termasuk URL Gambar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}