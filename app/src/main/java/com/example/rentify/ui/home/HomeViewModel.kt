package com.example.rentify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentify.data.remote.Vehicle
import com.example.rentify.data.repository.VehicleRepository
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val vehicles: List<Vehicle>) : HomeState()
    data class Error(val exception: Exception) : HomeState()
}

class HomeViewModel(private val repository: VehicleRepository) : ViewModel() {

    private val _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> get() = _homeState

    private var allVehicles: List<Vehicle> = emptyList()

    private val _filteredVehicles = MutableLiveData<List<Vehicle>>()
    val filteredVehicles: LiveData<List<Vehicle>> get() = _filteredVehicles

    private val _currentCategory = MutableLiveData<String>("Car")
    val currentCategory: LiveData<String> get() = _currentCategory

    fun fetchVehicles() {
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
            try {
                val vehicles = repository.getVehiclesFromCloud()
                allVehicles = vehicles
                _homeState.value = HomeState.Success(vehicles)
                filterVehicles(_currentCategory.value ?: "Car")
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(e)
            }
        }
    }

    fun filterVehicles(category: String) {
        _currentCategory.value = category
        val filtered = allVehicles.filter { it.category.equals(category, ignoreCase = true) }
        _filteredVehicles.value = filtered
    }
}
