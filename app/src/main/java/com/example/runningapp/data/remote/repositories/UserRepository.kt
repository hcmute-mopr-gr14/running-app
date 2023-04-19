package com.example.runningapp.data.remote.repositories

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*

interface UserRepository {
    suspend fun login(body: LoginRequestDTO) : ApiResponse<LoginResponseDataDTO>?
}