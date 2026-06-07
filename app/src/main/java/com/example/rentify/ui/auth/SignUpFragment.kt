package com.example.rentify.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.databinding.FragmentSignupBinding
import com.example.rentify.ui.ViewModelFactory

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: AuthViewModel by viewModels { factory }

        // Mengamati status pendaftaran dari ViewModel
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthResult.Loading -> {
                    binding.btnRegister.isEnabled = false
                }
                is AuthResult.Success -> {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(requireContext(), "Akun berhasil dibuat! Silakan Login", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is AuthResult.Error -> {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(requireContext(), "Gagal: ${state.exception.message}", Toast.LENGTH_SHORT).show()
                }
                is AuthResult.Idle -> {
                    binding.btnRegister.isEnabled = true
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etNameSignup.text.toString().trim()
            val email = binding.etEmailSignup.text.toString().trim()
            val password = binding.etPasswordSignup.text.toString().trim()

            // Validasi: Pastikan input tidak kosong
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi: Password minimal 6 karakter (Aturan baku Firebase)
            if (password.length < 6) {
                Toast.makeText(requireContext(), "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signUp(name, email, password)
        }

        // Teks "Log in" pindah ke halaman Sign In
        binding.tvToSignin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}