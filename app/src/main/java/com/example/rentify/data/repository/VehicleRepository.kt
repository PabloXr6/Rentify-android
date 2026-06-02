package com.example.rentify.data.repository

import com.example.rentify.data.local.FavoriteVehicleDao
import com.example.rentify.data.local.VehicleEntity

class VehicleRepository(private val favoriteVehicleDao: FavoriteVehicleDao) {

    suspend fun insertFavorite(vehicle: VehicleEntity) {
        favoriteVehicleDao.insertFavorite(vehicle)
    }

    suspend fun deleteFavorite(vehicle: VehicleEntity) {
        favoriteVehicleDao.deleteFavorite(vehicle)
    }

    suspend fun getAllFavorites(): List<VehicleEntity> {
        return favoriteVehicleDao.getAllFavorites()
    }

    suspend fun isFavorite(id: Int): Boolean {
        return favoriteVehicleDao.isFavorite(id)
    }
}