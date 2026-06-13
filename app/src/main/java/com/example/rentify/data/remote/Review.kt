package com.example.rentify.data.remote

data class Review(
    val id: String = "",
    val vehicleId: String = "",
    val vehicleName: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val date: String = ""
)
