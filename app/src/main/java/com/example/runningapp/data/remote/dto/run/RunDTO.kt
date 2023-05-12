package com.example.runningapp.data.remote.dto.run

import kotlinx.serialization.Serializable

@Serializable
data class RunDTO(
    val _id: String,
    val date: String,
    val rounds: List<RoundDTO> = listOf(),
) {
    @Serializable
    data class RoundDTO(
        val points: List<LatLngDTO> = listOf(),
        val meters: Double = 0.0,
        val seconds: Long = 0,
    ) {
        @Serializable
        data class LatLngDTO(
            val lat: Double = 0.0,
            val lng: Double = 0.0,
        )
    }
}