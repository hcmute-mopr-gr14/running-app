package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.repositories.RunRepository
import com.example.runningapp.data.repositories.UserRepository
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(private val runRepository: RunRepository, private val userRepository: UserRepository) {
    suspend fun getRuns() = runRepository.getAll()
    suspend fun getUser(id: ObjectId) = userRepository.getUser(id)
}