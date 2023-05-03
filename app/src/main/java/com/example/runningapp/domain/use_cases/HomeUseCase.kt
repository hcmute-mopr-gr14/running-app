package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.HomeResponseDataDTO
import com.example.runningapp.data.remote.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun home(): ApiResponse<HomeResponseDataDTO>? {
        return userRepository.home()
    }
}