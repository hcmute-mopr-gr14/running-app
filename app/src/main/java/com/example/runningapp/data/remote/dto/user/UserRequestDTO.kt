package com.example.runningapp.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
class UserRequestDTO(
    val imageUrl: ByteArray
)