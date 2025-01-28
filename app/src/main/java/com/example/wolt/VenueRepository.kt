package com.example.wolt

import javax.inject.Inject

interface VenueRepository {
    suspend fun getVenuesForLocation(latitude: Double, longitude: Double): List<Item>
}

class VenueRepoImpl @Inject constructor(
    private val apiService: ApiService
) : VenueRepository {
    override suspend fun getVenuesForLocation(latitude: Double, longitude: Double): List<Item> {
        return try {
            val response = apiService.getVenuesForLocation(latitude, longitude)
            response.sections
                .flatMap { it.items }
                .map { it }
        } catch (e: Exception) {
            throw e
        }
    }
}