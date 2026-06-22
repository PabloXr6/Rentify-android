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

    private val _singleShowroom = MutableLiveData<Showroom>()
    val singleShowroom: LiveData<Showroom> get() = _singleShowroom

    private val _showrooms = MutableLiveData<List<Showroom>>()
    val showrooms: LiveData<List<Showroom>> get() = _showrooms

    private val _vehicles = MutableLiveData<List<Vehicle>>()
    val vehicles: LiveData<List<Vehicle>> get() = _vehicles

    // ----------------------------------------------------------------
    // VEHICLE (KENDARAAN)
    // ----------------------------------------------------------------

    fun loadAllVehicles() {
        viewModelScope.launch {
            try {
                val result = repository.getVehicles()
                _vehicles.value = result
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal memuat kendaraan")
            }
        }
    }

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

    fun updateVehicle(vehicle: Vehicle) {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val success = repository.updateVehicle(vehicle)
                _state.value = if (success) AdminState.Success else AdminState.Error("Gagal memperbarui kendaraan")
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
    fun loadShowrooms() {
        viewModelScope.launch {
            try {
                val result = repository.getShowrooms()
                _showrooms.value = result
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Gagal memuat daftar showroom")
            }
        }
    }

    fun loadShowroomById(id: String) {
        viewModelScope.launch {
            try {
                val result = repository.getShowroomById(id)
                _singleShowroom.value = result
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

    fun deleteShowroom(showroomId: String) {
        _state.value = AdminState.Loading
        viewModelScope.launch {
            try {
                val success = repository.deleteShowroom(showroomId)
                _state.value = if (success) AdminState.Success else AdminState.Error("Gagal menghapus showroom")
            } catch (e: Exception) {
                _state.value = AdminState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun resetState() {
        _state.value = AdminState.Idle
    }
}