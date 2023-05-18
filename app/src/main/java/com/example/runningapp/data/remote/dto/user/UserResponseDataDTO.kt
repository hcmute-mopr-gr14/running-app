package com.example.runningapp.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDataDTO(
    val _id: String,
    val email: String,
    val nickname: String,
    val imageUrl: String
)
