package com.example.rentify.data.repository

import com.example.rentify.data.remote.Showroom
import com.example.rentify.data.remote.Vehicle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminRepository {

    private val db = FirebaseFirestore.getInstance()

    // ----------------------------------------------------------------
    // VEHICLE
    // ----------------------------------------------------------------
    suspend fun addVehicle(vehicle: Vehicle): Boolean {
        return try {
            db.collection("vehicles").add(vehicle).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteVehicle(vehicleId: String): Boolean {
        return try {
            db.collection("vehicles").document(vehicleId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // ----------------------------------------------------------------
    // SHOWROOM
    // ----------------------------------------------------------------

    // Mengambil satu showroom spesifik berdasarkan ID kendaraan
    suspend fun getShowroomById(showroomId: String): Showroom {
        return try {
            val snap = db.collection("showrooms").document(showroomId).get().await()
            snap.toObject(Showroom::class.java) ?: Showroom()
        } catch (e: Exception) {
            Showroom()
        }
    }

    // Mengambil semua daftar showroom untuk pilihan Dropdown di Form
    suspend fun getShowrooms(): List<Showroom> {
        return try {
            val snap = db.collection("showrooms").get().await()
            snap.map { doc ->
                doc.toObject(Showroom::class.java).copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Menyimpan Showroom Baru atau Mengedit Showroom Lama
    suspend fun updateShowroom(showroom: Showroom): Boolean {
        return try {
            if (showroom.id.isEmpty()) {
                // Jika ID kosong, buat dokumen baru dengan ID acak otomatis dari Firestore
                val docRef = db.collection("showrooms").document()
                val showroomWithId = showroom.copy(id = docRef.id)
                docRef.set(showroomWithId).await()
            } else {
                // Jika ID sudah ada, timpa dokumen lama (mode edit)
                db.collection("showrooms").document(showroom.id).set(showroom).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // Mengambil semua daftar kendaraan untuk list Admin
    suspend fun getVehicles(): List<Vehicle> {
        return try {
            val snap = db.collection("vehicles").get().await()
            snap.map { doc ->
                doc.toObject(Vehicle::class.java).copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Mengupdate data kendaraan yang sudah ada (Mode Edit)
    suspend fun updateVehicle(vehicle: Vehicle): Boolean {
        return try {
            db.collection("vehicles").document(vehicle.id).set(vehicle).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteShowroom(showroomId: String): Boolean {
        return try {
            db.collection("showrooms").document(showroomId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}