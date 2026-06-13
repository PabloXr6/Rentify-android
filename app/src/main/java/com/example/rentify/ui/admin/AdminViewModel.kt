package com.example.rentify.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentify.data.remote.Order
import com.example.rentify.data.remote.Review
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.data.repository.AdminRepository
import kotlinx.coroutines.launch

sealed class AdminState {
    object Idle : AdminState()
    object Loading : AdminState()
    object Success : AdminState()
    data class Error(val message: String) : AdminState()
}

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _state = MutableLiveData<AdminState>(AdminState.Idle)
    val state: LiveData<AdminState> get() = _state

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    // ----------------------------------------------------------------
    // VEHICLE
    // ----------------------------------------------------------------
    fun addVehicle(vehicle: Vehicle) {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val success = repository.addVehicle(vehicle)
                _state.value = if (success) AdminState.Success else AdminState.Error("Gagal menambahkan kendaraan")
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun deleteVehicle(vehicleId: String) {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val success = repository.deleteVehicle(vehicleId)
                _state.value = if (success) AdminState.Success else AdminState.Error("Gagal menghapus kendaraan")
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    // ----------------------------------------------------------------
    // ORDERS
    // ----------------------------------------------------------------
    fun loadAllOrders() {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getAllOrders()
                _orders.value = result
                _state.value = AdminState.Idle
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal memuat pesanan")
            }
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            try {
                repository.updateOrderStatus(orderId, status)
                loadAllOrders() // refresh list
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal update status")
            }
        }
    }

    // ----------------------------------------------------------------
    // REVIEWS
    // ----------------------------------------------------------------
    fun loadAllReviews() {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getAllReviews()
                _reviews.value = result
                _state.value = AdminState.Idle
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal memuat ulasan")
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            try {
                repository.deleteReview(reviewId)
                loadAllReviews() // refresh list
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal menghapus ulasan")
            }
        }
    }

    fun resetState() {
        _state.value = AdminState.Idle
    }
}
