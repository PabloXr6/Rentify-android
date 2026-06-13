package com.example.rentify.data.repository

import com.example.rentify.data.remote.Order
import com.example.rentify.data.remote.Review
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
    // ORDERS (PESANAN)
    // ----------------------------------------------------------------

    /**
     * Ambil semua pesanan dari Firestore.
     * TODO (Role 3): implementasi fetch dari collection "pesanan"
     */
    suspend fun getAllOrders(): List<Order> {
        // TODO: val result = db.collection("pesanan").get().await()
        //       return result.map { it.toObject(Order::class.java).copy(id = it.id) }
        return listOf(
            Order("1", "abc", "Toyota Avanza", "", "Budi", "budi@gmail.com", "10 Jun 2026", "12 Jun 2026", "pending", "Rp 800.000"),
            Order("2", "def", "Honda Beat", "", "Siti", "siti@gmail.com", "11 Jun 2026", "13 Jun 2026", "confirmed", "Rp 300.000")
        ) // dummy data
    }

    /**
     * Update status pesanan (pending/confirmed/completed/cancelled).
     * TODO (Role 3): implementasi update field "status" di Firestore
     */
    suspend fun updateOrderStatus(orderId: String, status: String): Boolean {
        // TODO: db.collection("pesanan").document(orderId).update("status", status).await()
        return true // dummy sukses
    }

    // ----------------------------------------------------------------
    // REVIEWS (ULASAN)
    // ----------------------------------------------------------------

    /**
     * Ambil semua ulasan dari Firestore.
     * TODO (Role 3): implementasi fetch dari collection "ulasan"
     */
    suspend fun getAllReviews(): List<Review> {
        // TODO: val result = db.collection("ulasan").get().await()
        //       return result.map { it.toObject(Review::class.java).copy(id = it.id) }
        return listOf(
            Review("1", "abc", "Toyota Avanza", "Budi", "budi@gmail.com", 4.5f, "Mobil bersih dan nyaman!", "10 Jun 2026"),
            Review("2", "def", "Honda Beat", "Siti", "siti@gmail.com", 5.0f, "Pelayanan sangat ramah!", "11 Jun 2026")
        ) // dummy data
    }

    /**
     * Hapus ulasan dari Firestore.
     * TODO (Role 3): implementasi hapus dari collection "ulasan"
     */
    suspend fun deleteReview(reviewId: String): Boolean {
        // TODO: db.collection("ulasan").document(reviewId).delete().await()
        return true // dummy sukses
    }
}
