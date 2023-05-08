package com.example.runningapp.data.remote.data_sources

import com.example.runningapp.data.entities.RunningLog
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(private val apiService: RunningApiService) {
    suspend fun fetchRunningLogs(): List<RunningLog> =
        when (val response = apiService.fetchHomeData()) {
            is ApiResponse.Data ->
                response.data.runningLogs.map {
                    RunningLog(
                        id = it._id,
                        seconds = it.seconds,
                        steps = it.steps,
                        distance = it.distance
                    )
                }

            else -> listOf()
        }
}