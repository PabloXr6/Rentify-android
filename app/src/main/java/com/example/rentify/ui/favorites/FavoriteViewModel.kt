package com.example.rentify.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentify.data.local.FavoriteVehicleDao
import com.example.rentify.data.local.VehicleEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val dao: FavoriteVehicleDao) : ViewModel() {

    private val _favoritesList = MutableLiveData<List<VehicleEntity>>()
    val favoritesList: LiveData<List<VehicleEntity>> get() = _favoritesList

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getAllFavorites() {
        viewModelScope.launch {
            val list = dao.getAllFavorites()
            _favoritesList.value = list
        }
    }

    fun addFavorite(vehicle: VehicleEntity) {
        viewModelScope.launch {
            dao.insertFavorite(vehicle)
            _isFavorite.value = true
            getAllFavorites()
        }
    }

    fun removeFavorite(vehicle: VehicleEntity) {
        viewModelScope.launch {
            dao.deleteFavorite(vehicle)
            _isFavorite.value = false
            getAllFavorites()
        }
    }

    fun checkIsFavorite(id: String) {
        viewModelScope.launch {
            val exists = dao.isFavorite(id)
            _isFavorite.value = exists
        }
    }
}
