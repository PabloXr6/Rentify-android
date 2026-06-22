package com.example.rentify.data.repository

import com.example.rentify.data.remote.Vehicle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VehicleRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getVehiclesFromCloud(): List<Vehicle> {
        return try {
            val result = db.collection("vehicles").get().await()
            result.mapNotNull { document ->
                try {
                    // Konversi dokumen Firestore ke objek Vehicle
                    val car = document.toObject(Vehicle::class.java)
                    // Ambil ID dokumen yang asli dari Firestore
                    car?.copy(id = document.id)
                } catch (e: Exception) {
                    // Log jika ada satu dokumen yang formatnya salah
                    null
                }
            }
        } catch (e: Exception) {
            // Lempar error agar bisa ditangkap di ViewModel dan ditampilkan di Toast
            throw e
        }
    }
}
