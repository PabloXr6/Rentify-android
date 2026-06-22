package com.example.rentify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "favorites")
data class VehicleEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: String,
    val rating: String,
    val imageUrl: String,
    val transmission: String = "",
    val seats: String = "",
    val category: String = "",
    val showroomId: String = ""
)