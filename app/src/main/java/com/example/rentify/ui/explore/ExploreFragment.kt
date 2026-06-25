package com.example.rentify.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var articleAdapter: ArticleAdapter

    // Menyambungkan Fragment dengan ViewModel
    private val viewModel: ExploreViewModel by viewModels()

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

        // Mengamati perubahan data dari ViewModel
        viewModel.exploreState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ExploreState.Loading -> {
                    // Status loading (bisa diabaikan sementara)
                }
                is ExploreState.Success -> {
                    // Jika sukses, masukkan daftar artikel ke dalam Adapter
                    articleAdapter.submitList(state.articles)
                }
                is ExploreState.Error -> {
                    // Jika gagal/error internet, tampilkan peringatan
                    Toast.makeText(requireContext(), "Gagal: ${state.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter()
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Perintahkan ViewModel untuk menarik data Firebase setiap kali halaman dibuka
        viewModel.fetchArticles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}