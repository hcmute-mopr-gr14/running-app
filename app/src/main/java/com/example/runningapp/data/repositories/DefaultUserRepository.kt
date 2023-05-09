package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.local.data_sources.UserLocalDataSource
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {
    private val runs: Flow<List<Run>> = userLocalDataSource.getAllRuns()
    override suspend fun getRuns(): Flow<List<Run>> = supervisorScope {
        launch {
            try {
                userLocalDataSource.upsert(remoteDataSource.fetchRuns())
            } catch (e: Exception) {
                Log.d("UserRepository", "Connection to remote failed, using local data source")
                Log.d("UserRepository", e.toString())
            }
        }
        runs
    }
}