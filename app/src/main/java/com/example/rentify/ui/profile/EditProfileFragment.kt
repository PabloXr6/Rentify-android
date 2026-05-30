package com.example.rentify.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackEdit.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp() // Langsung kembali setelah save
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}