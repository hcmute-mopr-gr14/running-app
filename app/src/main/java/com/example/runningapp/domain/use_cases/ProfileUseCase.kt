package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.repositories.UserRepository
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun getUser(id: ObjectId) = userRepository.getUser(id)
}