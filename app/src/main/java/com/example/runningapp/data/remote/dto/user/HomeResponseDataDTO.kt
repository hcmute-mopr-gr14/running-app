package com.example.runningapp.data.remote.dto.user
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDataDTO(
    val nickname: String,
    val runningLogs: List<RunningLogsDataDTO>
)

@Serializable
data class RunningLogsDataDTO(
    val _id: String,
    val seconds: Int,
    val steps: Int,
    val distance: Int,
    val date: String
)
