package com.example.rentify.data.repository

import com.example.rentify.data.remote.Vehicle
import com.google.firebase.firestore.FirebaseFirestore

class VehicleRepository {

    // Panggil Firebase di dalam Repository
    private val db = FirebaseFirestore.getInstance()

    // Fungsi ini akan dipanggil oleh UI (Fragment)
    fun getVehiclesFromCloud(
        onSuccess: (List<Vehicle>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("vehicles").get()
            .addOnSuccessListener { result ->
                val vehicleList = mutableListOf<Vehicle>()
                for (document in result) {
                    val vehicle = document.toObject(Vehicle::class.java)
                    vehicleList.add(vehicle)
                }
                // Jika sukses, kembalikan daftar mobil/motornya
                onSuccess(vehicleList)
            }
            .addOnFailureListener { exception ->
                // Jika gagal, kembalikan pesan errornya
                onFailure(exception)
            }
    }
}