package com.example.rentify.data.repository

import com.example.rentify.data.remote.Vehicle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VehicleRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getVehiclesFromCloud(): List<Vehicle> {
        val result = db.collection("vehicles").get().await()
        return result.map { document ->
            document.toObject(Vehicle::class.java).copy(id = document.id)
        }
    }
}