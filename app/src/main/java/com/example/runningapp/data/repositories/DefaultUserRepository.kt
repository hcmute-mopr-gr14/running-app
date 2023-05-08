package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.entities.RunningLog
import com.example.runningapp.data.local.daos.RunningLogDao
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val runningLogDao: RunningLogDao,
) : UserRepository {
    private val runningLogs: Flow<List<RunningLog>> = runningLogDao.getAll()
    override suspend fun getRunningLogs(): Flow<List<RunningLog>> = supervisorScope {
        launch {
            try {
                runningLogDao.upsert(*remoteDataSource.fetchRunningLogs().toTypedArray())
            } catch (e: Exception) {
                Log.d("UserRepository", "Connection to remote failed, using local data source")
                Log.d("UserRepository", e.toString())
            }
        }
        runningLogs
    }
}