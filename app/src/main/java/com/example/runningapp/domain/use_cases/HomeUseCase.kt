package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.repositories.RunRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(private val runRepository: RunRepository) {
    suspend fun getRuns() = runRepository.getAll()
}