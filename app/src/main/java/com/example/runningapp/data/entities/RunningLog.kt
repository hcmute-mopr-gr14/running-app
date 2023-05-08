package com.example.runningapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_logs")
data class RunningLog(
    @PrimaryKey val id: String,
    val seconds: Int,
    val steps: Int,
    val distance: Float,
)