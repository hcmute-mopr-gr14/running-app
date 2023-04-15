package com.example.runningapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
open class ApiResponse<T>(
    open val apiVersion: String,
    open val data: T? = null,
    open val error: ApiError? = null
)

@Serializable
data class ApiError(
    val code: Int,
    val message: String
)