package com.example.runningapp.data.remote.repositories

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*

interface UserRepository {
<<<<<<< HEAD
    suspend fun login(body: LoginRequestDTO) : ApiResponse<LoginResponseDataDTO>?
=======
    suspend fun login(body: LoginRequest) : ApiResponse<LoginResponseData>?
    suspend fun signup(body: SignupRequest) : ApiResponse<SignupResponseData>?
>>>>>>> 0a4ed7c (add signup usecase)
}