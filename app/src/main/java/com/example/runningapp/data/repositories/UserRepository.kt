package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.Run
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getRuns(): Flow<List<Run>>
}