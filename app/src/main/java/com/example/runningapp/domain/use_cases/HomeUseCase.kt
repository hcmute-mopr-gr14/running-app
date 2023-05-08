package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.entities.RunningLog
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.HomeResponseDataDTO
import com.example.runningapp.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun getRunningLogs() = userRepository.getRunningLogs()
}