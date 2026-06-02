package com.example.rentify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vehicles")
data class VehicleEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Int,
    val rating: String,
    val imageUrl: String
)