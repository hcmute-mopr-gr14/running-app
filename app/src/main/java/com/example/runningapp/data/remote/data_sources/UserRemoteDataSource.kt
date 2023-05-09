package com.example.runningapp.data.remote.data_sources

import com.example.runningapp.data.models.RunningLog
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.dto.ApiResponse
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(private val apiService: RunningApiService) {
    suspend fun fetchRunningLogs(): List<RunningLog> =
        when (val response = apiService.fetchHomeData()) {
            is ApiResponse.Data ->
                response.data.runningLogs.map {
                    RunningLog().apply {
                        _id = ObjectId(it._id)
                        seconds = it.seconds
                        steps = it.steps
                        distance = it.distance
                    }
                }

            else -> listOf()
        }
}