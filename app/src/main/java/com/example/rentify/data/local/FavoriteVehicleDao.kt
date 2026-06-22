package com.example.rentify.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteVehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(vehicle: VehicleEntity)

    @Delete
    suspend fun deleteFavorite(vehicle: VehicleEntity)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<VehicleEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}