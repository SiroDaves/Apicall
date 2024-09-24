package com.example.wolt.data.sources.remote

import com.example.wolt.data.models.WoltResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("restaurants")
    suspend fun getVenuesForLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WoltResponse
}