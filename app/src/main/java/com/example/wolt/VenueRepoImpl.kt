package com.example.wolt

import javax.inject.Inject

class VenueRepoImpl @Inject constructor(
    private val apiService: ApiService
) : VenueRepository {

    override suspend fun getVenuesForLocation(latitude: Double, longitude: Double): List<Venue> {
        return apiService.getVenuesForLocation(latitude, longitude)
    }
}