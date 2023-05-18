package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.repositories.FriendRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendUseCase @Inject constructor(private val friendRepository: FriendRepository) {
    suspend fun getFriends() = friendRepository.getFriends()
}