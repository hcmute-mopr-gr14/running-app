package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.Run
import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.UserRequestDTO
import com.example.runningapp.data.remote.dto.user.UserResponseDataDTO
import kotlinx.coroutines.flow.Flow

<<<<<<< Updated upstream
interface UserRepository {}
=======
interface UserRepository {
    suspend fun getRuns(): Flow<List<Run>>
    suspend fun getUser(): Flow<List<User>>
}
>>>>>>> Stashed changes
