package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun getRunningLogs() = userRepository.getRunningLogs()
}