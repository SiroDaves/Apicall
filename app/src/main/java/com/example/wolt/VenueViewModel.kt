package com.example.wolt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VenueViewModel @Inject constructor(
    application: Application,
    private val apiService: APIService
) : AndroidViewModel(application) {

    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> = _venues

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val locations = listOf(
        Pair(60.169418, 24.931618),
        Pair(60.169818, 24.932906),
        Pair(60.170005, 24.935105),
        Pair(60.169108, 24.936210),
        Pair(60.168355, 24.934869),
        Pair(60.167560, 24.932562),
        Pair(60.168254, 24.931532),
        Pair(60.169012, 24.930341),
        Pair(60.170085, 24.929569)
    )

    private suspend fun getVenuesForLocation(latitude: Double, longitude: Double) {
        _loading.emit(true)
        try {
            val response = apiService.getVenuesForLocation(latitude, longitude)
            _venues.emit(response)
            _error.emit(null) // Clear any previous errors
        } catch (e: Exception) {
            _venues.emit(emptyList())
            _error.emit(e.message ?: "An error occurred while fetching venues")
        } finally {
            _loading.emit(false)
        }
    }

    fun startVenueFetchCycle() {
        viewModelScope.launch {
            var index = 0
            while (isActive) {
                val (latitude, longitude) = locations[index % locations.size]
                getVenuesForLocation(latitude, longitude)

                index++
                delay(10_000)
            }
        }
    }
}

