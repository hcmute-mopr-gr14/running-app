package com.example.runningapp.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RunResponseDataDTO(
    val _id: String,
    val rounds: List<RoundDTO>,
) {
    @Serializable
    data class RoundDTO(
        val points: List<List<Double>>,
        val meters: Double,
        val seconds: Long,
    )
}
