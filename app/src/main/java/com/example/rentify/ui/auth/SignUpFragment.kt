package com.example.rentify.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rentify.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    // Variabel untuk memanggil Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

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

            // Memanggil fungsi Firebase untuk mendaftarkan akun baru
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Jika sukses, tampilkan pesan dan kembali ke halaman Login
                        Toast.makeText(requireContext(), "Akun berhasil dibuat! Silakan Login", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    } else {
                        // Jika gagal (misal email sudah terdaftar), tampilkan pesan error
                        Toast.makeText(requireContext(), "Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
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