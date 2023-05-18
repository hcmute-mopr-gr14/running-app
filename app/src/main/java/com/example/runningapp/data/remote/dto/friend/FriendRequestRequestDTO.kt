package com.example.runningapp.data.remote.dto.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestRequestDTO(
    val email: String = "",
)