package com.example.runningapp.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDTO(
    val email: String,
    val password: String,
    val confirmpassword: String,
)