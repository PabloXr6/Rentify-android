package com.example.rentify.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentFavoritesBinding
import com.example.rentify.ui.ViewModelFactory

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: FavoriteViewModel by viewModels { factory }

        favoriteAdapter = FavoriteAdapter()
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }

        viewModel.favoritesList.observe(viewLifecycleOwner) { favorites ->
            favoriteAdapter.submitList(favorites)
        }

        viewModel.getAllFavorites()
    }

    override fun onResume() {
        super.onResume()
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: FavoriteViewModel by viewModels { factory }
        viewModel.getAllFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}