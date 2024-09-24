package com.example.wolt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wolt.data.models.Item
import com.example.wolt.domain.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    private val _venues = MutableStateFlow<List<Item>>(emptyList())
    val venues: StateFlow<List<Item>> get() = _venues

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val startLocation = 60.170187 to 24.930599

    private val locations = listOf(
        60.170187 to 24.930599,  // Center point
        60.170187 to 24.932599,  // East
        60.168187 to 24.930599,  // South
        60.170187 to 24.928599,  // West
        60.172187 to 24.930599,  // North
        60.171187 to 24.931599,  // Northeast
        60.169187 to 24.931599,  // Southeast
        60.169187 to 24.929599,  // Southwest
        60.171187 to 24.929599   // Northwest
    )

    private suspend fun getVenuesForLocationWithRetry(
        latitude: Double,
        longitude: Double,
        maxRetries: Int = 3,
        retryDelay: Long = 5_000L
    ) {
        var currentAttempt = 0

        while (currentAttempt < maxRetries) {
            try {
                val venues = withContext(Dispatchers.IO) {
                    venueRepository.getVenuesForLocation(latitude, longitude)
                }
                // Only emit new venues if we successfully fetched them
                _venues.emit(venues)
                _error.emit(null)
                return
            } catch (e: Exception) {
                currentAttempt++
                if (currentAttempt >= maxRetries) {
                    _error.emit("Failed to fetch venues: ${e.message}")
                } else {
                    delay(retryDelay)
                }
            }
        }
    }

    fun startVenueFetchCycle() {
        viewModelScope.launch {
            var locationIndex = 0

            while (isActive) {
                _loading.emit(true)

                // Get current location coordinates
                val (latitude, longitude) = locations[locationIndex % locations.size]

                // Fetch venues for current location
                getVenuesForLocationWithRetry(latitude, longitude)

                _loading.emit(false)

                // Move to next location in the circle
                locationIndex = (locationIndex + 1) % locations.size

                // Wait before fetching next location
                delay(10_000) // 10 seconds delay between location changes
            }
        }
    }
}