package com.example.rentify.ui.admin

import com.example.rentify.data.remote.Article
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.R
import com.example.rentify.databinding.FragmentAdminManageArticleBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminManageArticleFragment : Fragment(R.layout.fragment_admin_manage_article) {

    private var _binding: FragmentAdminManageArticleBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: AdminArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminManageArticleBinding.bind(view)

        setupRecyclerView()
        loadArticles()

        binding.btnBackManageArticle.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupRecyclerView() {
        adapter = AdminArticleAdapter(
            onEditClick = { article ->
                // Aksi Edit: Arahkan ke form edit (bisa pakai fragment yang sama dengan Add)
            },
            onDeleteClick = { article ->
                // Aksi Hapus: Langsung hapus dari Firestore
                db.collection("explore_articles").document(article.id).delete()
                    .addOnSuccessListener { loadArticles() } // Refresh daftar setelah hapus
            }
        )
        binding.rvAdminArticles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAdminArticles.adapter = adapter
    }

    private fun loadArticles() {
        db.collection("explore_articles").get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Article>()
                for (document in result) {
                    val article = document.toObject(Article::class.java)
                    article.id = document.id
                    list.add(article)
                }
                adapter.submitList(list)
            }
            .addOnFailureListener { e ->
                android.widget.Toast.makeText(requireContext(), "Gagal memuat: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}