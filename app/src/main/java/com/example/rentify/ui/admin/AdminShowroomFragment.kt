package com.example.rentify.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.data.remote.Showroom
import com.example.rentify.databinding.FragmentAdminShowroomBinding
import com.example.rentify.ui.ViewModelFactory

class AdminShowroomFragment : Fragment() {

    private var _binding: FragmentAdminShowroomBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    // Variabel penanda Mode Edit
    private var isEditMode = false
    private var showroomId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminShowroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cek apakah ada data kiriman (Berarti Mode Edit!)
        arguments?.let {
            if (it.containsKey("id")) {
                isEditMode = true
                showroomId = it.getString("id") ?: ""

                // Tempel data lama ke form
                binding.etShowroomName.setText(it.getString("name"))
                binding.etShowroomPhone.setText(it.getString("phone"))
                binding.etShowroomDesc.setText(it.getString("description"))

                binding.btnSaveShowroom.text = "Perbarui Showroom"
            }
        }

        binding.btnBackShowroom.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveShowroom.setOnClickListener {
            val name = binding.etShowroomName.text.toString().trim()
            val phone = binding.etShowroomPhone.text.toString().trim()
            val desc = binding.etShowroomDesc.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || desc.isEmpty()) {
                Toast.makeText(requireContext(), "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kalau mode Edit, ID lama dipakai. Kalau Tambah baru, ID-nya kosong
            val updatedShowroom = Showroom(
                id = showroomId,
                name = name,
                phone = phone,
                description = desc
            )

            viewModel.updateShowroom(updatedShowroom)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdminState.Loading -> {
                    binding.btnSaveShowroom.isEnabled = false
                    binding.btnSaveShowroom.text = "Menyimpan..."
                }
                is AdminState.Success -> {
                    val msg = if (isEditMode) "Showroom berhasil diperbarui!" else "Showroom baru berhasil didaftarkan!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                    findNavController().navigateUp()
                }
                is AdminState.Error -> {
                    binding.btnSaveShowroom.isEnabled = true
                    binding.btnSaveShowroom.text = if (isEditMode) "Perbarui Showroom" else "Simpan Informasi Showroom"
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                else -> {
                    binding.btnSaveShowroom.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}