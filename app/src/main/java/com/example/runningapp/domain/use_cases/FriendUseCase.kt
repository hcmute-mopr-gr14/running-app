package com.example.runningapp.domain.use_cases

import android.util.Patterns
import com.example.runningapp.data.remote.dto.friend.FriendRequestRequestDTO
import com.example.runningapp.data.remote.services.FriendApiService
import com.example.runningapp.data.repositories.FriendRepository
import com.example.runningapp.domain.models.Validation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
    private val friendApiService: FriendApiService
) {
    suspend fun getFriends() = friendRepository.getFriends()
    suspend fun getIncomingFriends() = friendRepository.getIncomingFriends()
    suspend fun sendFriendRequest(email: String) =
        friendApiService.postFriendRequest(FriendRequestRequestDTO(email = email))

    fun validateEmail(email: String): Validation {
        if (email.isBlank()) {
            return Validation.Error(message = "Invalid email")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Validation.Error(message = "Invalid email")
        }
        return Validation.Success
    }
}