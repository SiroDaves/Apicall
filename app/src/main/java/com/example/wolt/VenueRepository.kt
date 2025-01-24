package com.example.wolt


interface VenueRepository {
    suspend fun getVenuesForLocation(latitude: Double, longitude: Double): List<Venue>
}