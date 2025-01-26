package com.example.wolt


import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("venues")  // Assuming this is your endpoint
    suspend fun getVenuesForLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): List<Venue>
}