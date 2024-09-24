package com.example.wolt.domain

import com.example.wolt.data.models.Item
import com.example.wolt.data.models.Section
import com.example.wolt.data.models.WoltResponse
import com.example.wolt.data.sources.remote.ApiService
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
            WoltResponse.sections
                .flatMap { Section.items }
                .map { it }
        } catch (e: Exception) {
            throw e
        }
    }
}