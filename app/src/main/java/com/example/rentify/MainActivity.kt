package com.example.rentify

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.rentify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation Component ke Bottom Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)

        // --- LOGIKA UNTUK MENYEMBUNYIKAN BOTTOM NAV ---
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Jika sedang di halaman Splash atau Sign In, sembunyikan navigasi bawah
                R.id.splashFragment, R.id.signInFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                }
                // Jika di halaman lain (Home, Explore, dll), tampilkan kembali
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }
}