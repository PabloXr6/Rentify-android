package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.databinding.FragmentAdminReviewsBinding
import com.example.rentify.ui.ViewModelFactory

class AdminReviewsFragment : Fragment() {

    private var _binding: FragmentAdminReviewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: AdminReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackReviews.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = AdminReviewAdapter(
            onDelete = { review ->
                viewModel.deleteReview(review.id)
                Toast.makeText(requireContext(), "Ulasan dari ${review.userName} dihapus", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReviews.adapter = adapter

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            adapter.submitList(reviews)
        }

        viewModel.loadAllReviews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
