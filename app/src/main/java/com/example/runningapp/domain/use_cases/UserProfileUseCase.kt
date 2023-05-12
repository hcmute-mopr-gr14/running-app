package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.UserRequestDTO
import com.example.runningapp.data.remote.dto.user.UserResponseDataDTO
import com.example.runningapp.data.repositories.UserRepository
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun getUser() = userRepository.getUser()

    suspend fun updateAvatarOnApi(imageData: ByteArray): ApiResponse<UserResponseDataDTO>? {
        val requestDto = UserRequestDTO(imageUrl = imageData)
        return userRepository.updateAvatar(requestDto)
    }
}