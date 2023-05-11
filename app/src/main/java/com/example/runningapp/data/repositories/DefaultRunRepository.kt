package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.local.data_sources.RunLocalDataSource
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.remote.data_sources.RunRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRunRepository @Inject constructor(
    private val remoteDataSource: RunRemoteDataSource,
    private val localDataSource: RunLocalDataSource,
) : RunRepository {
    private val all: Flow<List<Run>> = localDataSource.getAll()
    override suspend fun getAll(): Flow<List<Run>> = supervisorScope {
        launch {
            try {
                localDataSource.upsertRuns(remoteDataSource.fetchRuns())
            } catch (e: Exception) {
                Log.d("RunRepository", "Connection to remote failed, using local data source")
                Log.d("RunRepository", e.toString())
            }
        }
        all
    }

    override suspend fun addRound(date: LocalDate, round: Run.Round): Run? = supervisorScope {
        launch {
            try {
                remoteDataSource.addRound(date, round)
            } catch (e: Exception) {
                Log.d("RunRepository", "Connection to remote failed, using local data source")
                Log.d("RunRepository", e.toString())
            }
        }
        localDataSource.addRound(date, round)
    }
}