package com.example.rentify.data.repository

import com.example.rentify.data.remote.Showroom
import com.example.rentify.data.remote.Vehicle

/**
 * AdminRepository — stub untuk fitur admin.
 *
 * ========================== UNTUK ROLE 3 ==========================
 * Semua fungsi di bawah sudah siap tinggal diisi implementasi Firestore-nya.
 * Ganti setiap baris "return ..." dengan kode Firebase yang sesuai.
 * Contoh pola:
 *   val result = db.collection("vehicles").get().await()
 *   return result.map { it.toObject(Vehicle::class.java).copy(id = it.id) }
 * ==================================================================
 */
class AdminRepository {

    // ----------------------------------------------------------------
    // VEHICLE
    // ----------------------------------------------------------------

    /**
     * Tambah kendaraan baru ke Firestore.
     * TODO (Role 3): implementasi simpan ke collection "vehicles"
     */
    suspend fun addVehicle(vehicle: Vehicle): Boolean {
        // TODO: db.collection("vehicles").add(vehicle).await()
        return true // dummy sukses
    }

    /**
     * Hapus kendaraan dari Firestore.
     * TODO (Role 3): implementasi hapus dari collection "vehicles"
     */
    suspend fun deleteVehicle(vehicleId: String): Boolean {
        // TODO: db.collection("vehicles").document(vehicleId).delete().await()
        return true // dummy sukses
    }

    // ----------------------------------------------------------------
    // SHOWROOM
    // ----------------------------------------------------------------

    /**
     * Ambil info showroom dari Firestore.
     * TODO (Role 3): val snap = db.collection("showroom").document("main").get().await()
     *                return snap.toObject(Showroom::class.java) ?: Showroom()
     */
    suspend fun getShowroom(): Showroom {
        return Showroom() // data default
    }

    /**
     * Update info showroom ke Firestore.
     * TODO (Role 3): db.collection("showroom").document("main").set(showroom).await()
     */
    suspend fun updateShowroom(showroom: Showroom): Boolean {
        // TODO: db.collection("showroom").document("main").set(showroom).await()
        return true // dummy sukses
    }

}

