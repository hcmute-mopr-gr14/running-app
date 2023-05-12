package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.Run
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RunRepository {
    suspend fun getAll(): Flow<List<Run>>
    suspend fun addRound(date: LocalDate, round: Run.Round): Boolean
}