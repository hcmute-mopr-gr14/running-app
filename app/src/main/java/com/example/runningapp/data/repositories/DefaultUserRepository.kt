package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.models.RunningLog
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
    private val runningLogs: Flow<List<RunningLog>> = userLocalDataSource.getAll()
    override suspend fun getRunningLogs(): Flow<List<RunningLog>> = supervisorScope {
        launch {
            try {
                userLocalDataSource.upsert(remoteDataSource.fetchRunningLogs())
            } catch (e: Exception) {
                Log.d("UserRepository", "Connection to remote failed, using local data source")
                Log.d("UserRepository", e.toString())
            }
        }
        runningLogs
    }
}