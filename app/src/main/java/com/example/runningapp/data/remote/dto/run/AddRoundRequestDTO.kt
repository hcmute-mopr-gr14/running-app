package com.example.runningapp.data.remote.dto.run

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class AddRoundRequestDTO(
    val date: LocalDate,
    val round: RoundDTO
) {
    @Serializable
    data class RoundDTO(
        val points: List<LatLngDTO>,
        val meters: Double,
        val seconds: Long,
    ) {
        @Serializable
        data class LatLngDTO(
            val lat: Double,
            val lng: Double,
        )
    }
}
