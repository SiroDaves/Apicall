package com.example.wolt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> get() = _venues

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val locations = listOf(
        60.169418 to 24.931618,
        60.169818 to 24.932906,
        60.170005 to 24.935105,
        60.169108 to 24.936210,
        60.168355 to 24.934869,
        60.167560 to 24.932562,
        60.168254 to 24.931532,
        60.169012 to 24.930341,
        60.170085 to 24.929569
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
                // Perform the network call or database operation
                val response = withContext(Dispatchers.IO) {
                    venueRepository.getVenuesForLocation(latitude, longitude)
                }
                _venues.emit(response) // Update venues list
                _error.emit(null) // Clear error
                return // Exit after success
            } catch (e: Exception) {
                currentAttempt++
                if (currentAttempt >= maxRetries) {
                    _venues.emit(emptyList()) // Clear venues list on failure
                    _error.emit("Error: ${e.localizedMessage ?: "Failed after $maxRetries retries"}")
                } else {
                    delay(retryDelay) // Wait before retrying
                }
            }
        }
    }
    fun startVenueFetchCycle() {
        // Launch in the background using Dispatchers.IO to handle the task off the main thread
        viewModelScope.launch(Dispatchers.IO) {
            var index = 0
            while (isActive) {
                val (latitude, longitude) = locations[index % locations.size]

                // Emit loading state (this is still done in the IO context)
                _loading.emit(true) // Indicate loading

                // Perform the venue fetch with retry (assuming getVenuesForLocationWithRetry is a suspending function)
                getVenuesForLocationWithRetry(latitude, longitude)

                // Emit loading state (after fetching)
                _loading.emit(false) // Stop loading for this location

                index++

                // Delay between fetches, still in the background
                delay(10_000L) // Wait 10 seconds before the next fetch
            }
        }
    }

}


