package com.example.runningapp.data.remote.dto.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
    val _id: String = "",
    val email: String = "",
    val nickname: String = "",
    val imageUrl: String = "",
)