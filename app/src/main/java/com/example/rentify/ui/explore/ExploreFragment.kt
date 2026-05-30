package com.example.rentify.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadDummyArticles()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter()
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }

    private fun loadDummyArticles() {
        // Data artikel wisata lokal untuk mengetes UI
        val dummyList = listOf(
            DummyArticle(1, "5 Pantai Tersembunyi di Lombok untuk Liburan Tenang", "Destinasi", "28 Mei 2026 • 4 Min Read"),
            DummyArticle(2, "Tips Menyewa Mobil Matic untuk Keliling Senggigi", "Tips Wisata", "25 Mei 2026 • 3 Min Read"),
            DummyArticle(3, "Rute Terbaik Menuju Sembalun Menggunakan Motor", "Rute", "20 Mei 2026 • 5 Min Read"),
            DummyArticle(4, "Daftar Kuliner Khas NTB yang Wajib Dicoba Saat Roadtrip", "Kuliner", "15 Mei 2026 • 6 Min Read")
        )

        articleAdapter.submitList(dummyList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}