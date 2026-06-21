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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminShowroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackShowroom.setOnClickListener {
            findNavController().navigateUp()
        }

        // Muat data showroom saat ini
        viewModel.loadShowroom()

        // Isi form dengan data yang ada
        viewModel.showroom.observe(viewLifecycleOwner) { showroom ->
            binding.etShowroomName.setText(showroom.name)
            binding.etShowroomAddress.setText(showroom.address)
            binding.etShowroomPhone.setText(showroom.phone)
            binding.etShowroomHours.setText(showroom.openHours)
            binding.etShowroomDesc.setText(showroom.description)
        }

        // Observer state (loading/sukses/error)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AdminState.Loading -> {
                    binding.btnSaveShowroom.isEnabled = false
                    binding.btnSaveShowroom.text = "Menyimpan..."
                }
                is AdminState.Success -> {
                    binding.btnSaveShowroom.isEnabled = true
                    binding.btnSaveShowroom.text = "Simpan Informasi Showroom"
                    Toast.makeText(requireContext(), "✅ Informasi showroom berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                is AdminState.Error -> {
                    binding.btnSaveShowroom.isEnabled = true
                    binding.btnSaveShowroom.text = "Simpan Informasi Showroom"
                    Toast.makeText(requireContext(), "❌ ${state.message}", Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                else -> {
                    binding.btnSaveShowroom.isEnabled = true
                    binding.btnSaveShowroom.text = "Simpan Informasi Showroom"
                }
            }
        }

        // Simpan showroom
        binding.btnSaveShowroom.setOnClickListener {
            val name = binding.etShowroomName.text.toString().trim()
            val address = binding.etShowroomAddress.text.toString().trim()
            val phone = binding.etShowroomPhone.text.toString().trim()
            val hours = binding.etShowroomHours.text.toString().trim()
            val desc = binding.etShowroomDesc.text.toString().trim()

            if (name.isEmpty()) {
                binding.tilShowroomName.error = "Nama showroom tidak boleh kosong"
                return@setOnClickListener
            }
            binding.tilShowroomName.error = null

            if (phone.isEmpty()) {
                binding.tilShowroomPhone.error = "Nomor WhatsApp tidak boleh kosong"
                return@setOnClickListener
            }
            binding.tilShowroomPhone.error = null

            val showroom = Showroom(
                name = name,
                address = address,
                phone = phone,
                openHours = hours,
                description = desc
            )
            viewModel.updateShowroom(showroom)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
