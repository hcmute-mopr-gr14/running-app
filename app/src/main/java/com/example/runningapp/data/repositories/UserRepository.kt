package com.example.runningapp.data.repositories

import com.example.runningapp.data.entities.RunningLog
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getRunningLogs(): Flow<List<RunningLog>>
}