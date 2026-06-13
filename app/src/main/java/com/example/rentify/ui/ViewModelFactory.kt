package com.example.rentify.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rentify.data.local.RentifyDatabase
import com.example.rentify.data.pref.UserPreferences
import com.example.rentify.data.repository.AdminRepository
import com.example.rentify.data.repository.VehicleRepository
import com.example.rentify.ui.admin.AdminViewModel
import com.example.rentify.ui.auth.AuthViewModel
import com.example.rentify.ui.favorites.FavoriteViewModel
import com.example.rentify.ui.home.HomeViewModel

class ViewModelFactory(
    private val userPreferences: UserPreferences,
    private val vehicleRepository: VehicleRepository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(vehicleRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                val database = RentifyDatabase.getDatabase(context)
                FavoriteViewModel(database.favoriteVehicleDao()) as T
            }
            modelClass.isAssignableFrom(AdminViewModel::class.java) -> {
                AdminViewModel(AdminRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val sharedPref = UserPreferences(context)
                val repo = VehicleRepository()
                instance ?: ViewModelFactory(sharedPref, repo, context.applicationContext).also { instance = it }
            }
        }
    }
}
