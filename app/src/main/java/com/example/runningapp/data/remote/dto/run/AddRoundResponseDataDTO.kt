package com.example.runningapp.data.remote.dto.run

import kotlinx.serialization.Serializable

@Serializable
data class AddRoundResponseDataDTO(
    val acknowledged: Boolean,
    val modifiedCount: Int,
    val upsertedId: String?,
    val upsertedCount: Int,
    val matchedCount: Int
)
