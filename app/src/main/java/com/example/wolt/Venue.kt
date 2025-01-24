package com.example.wolt



import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    val id: String,
    val name: String,
    val short_description: String,
    val url: String
)