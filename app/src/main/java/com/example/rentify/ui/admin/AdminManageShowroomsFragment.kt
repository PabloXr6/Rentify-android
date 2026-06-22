package com.example.rentify.ui.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentify.R
import com.example.rentify.data.remote.Showroom
import com.example.rentify.databinding.FragmentAdminManageShowroomsBinding
import com.example.rentify.ui.ViewModelFactory

class AdminManageShowroomsFragment : Fragment() {

    private var _binding: FragmentAdminManageShowroomsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: AdminShowroomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageShowroomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.loadShowrooms()
        viewModel.showrooms.observe(viewLifecycleOwner) { showrooms ->
            adapter.submitList(showrooms)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdminState.Success -> {
                    viewModel.loadShowrooms()
                    viewModel.resetState()
                }
                is AdminState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                else -> {}
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fabAddShowroom.setOnClickListener {
            findNavController().navigate(R.id.adminShowroomFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = AdminShowroomAdapter(
            onEditClick = { showroom ->
                val bundle = Bundle().apply {
                    putString("id", showroom.id)
                    putString("name", showroom.name)
                    putString("phone", showroom.phone)
                    putString("description", showroom.description)
                }
                findNavController().navigate(R.id.adminShowroomFragment, bundle)
            },
            onDeleteClick = { showroom ->
                showDeleteConfirmation(showroom)
            }
        )

        binding.rvAdminShowrooms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@AdminManageShowroomsFragment.adapter
        }
    }

    private fun showDeleteConfirmation(showroom: Showroom) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Showroom")
            .setMessage("Yakin ingin menghapus ${showroom.name}? Pastikan tidak ada kendaraan yang sedang terhubung ke showroom ini.")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteShowroom(showroom.id)
                Toast.makeText(requireContext(), "Menghapus...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}