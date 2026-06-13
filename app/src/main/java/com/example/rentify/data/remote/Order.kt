package com.example.rentify.data.remote

data class Order(
    val id: String = "",
    val vehicleId: String = "",
    val vehicleName: String = "",
    val vehicleImageUrl: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val rentDate: String = "",
    val returnDate: String = "",
    val status: String = "pending", // pending, confirmed, completed, cancelled
    val totalPrice: String = "",
    val createdAt: String = ""
)
