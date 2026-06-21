package com.example.rentify.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentify.data.remote.Showroom
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

    private val _showroom = MutableLiveData<Showroom>()
    val showroom: LiveData<Showroom> get() = _showroom

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
    // SHOWROOM
    // ----------------------------------------------------------------
    fun loadShowroom() {
        viewModelScope.launch {
            try {
                val result = repository.getShowroom()
                _showroom.value = result
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal memuat data showroom")
            }
        }
    }

    fun updateShowroom(showroom: Showroom) {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val success = repository.updateShowroom(showroom)
                _state.value = if (success) AdminState.Success else AdminState.Error("Gagal menyimpan showroom")
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun resetState() {
        _state.value = AdminState.Idle
    }
}
