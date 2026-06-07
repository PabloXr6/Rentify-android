package com.example.rentify.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentify.R
import com.example.rentify.databinding.FragmentSigninBinding
import com.example.rentify.ui.ViewModelFactory

class SignInFragment : Fragment() {
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel: AuthViewModel by viewModels { factory }

        // Mengamati status login dari ViewModel
        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthResult.Loading -> {
                    binding.btnLogin.isEnabled = false
                    // Bisa ditambahkan progress bar jika ada di layout, tapi pastikan tombol disabled dulu
                }
                is AuthResult.Success -> {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(requireContext(), "Login Berhasil!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homeFragment)
                }
                is AuthResult.Error -> {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(requireContext(), "Login Gagal: ${state.exception.message}", Toast.LENGTH_SHORT).show()
                }
                is AuthResult.Idle -> {
                    binding.btnLogin.isEnabled = true
                }
            }
        }

        // Logika untuk tombol Log in
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validasi: Pastikan email dan password diisi
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signIn(email, password)
        }

        // Logika untuk teks "Sign up" (Pindah ke halaman Daftar)
        binding.tvToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}