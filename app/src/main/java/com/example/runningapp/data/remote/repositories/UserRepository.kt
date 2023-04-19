package com.example.runningapp.data.remote.repositories

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*

interface UserRepository {
<<<<<<< HEAD
    suspend fun login(body: LoginRequestDTO) : ApiResponse<LoginResponseDataDTO>?
<<<<<<< HEAD
    suspend fun signup(body: SignupRequestDTO) : ApiResponse<SignupResponseDataDTO>?

=======
=======
    suspend fun login(body: LoginRequest) : ApiResponse<LoginResponseData>?
    suspend fun signup(body: SignupRequest) : ApiResponse<SignupResponseData>?
>>>>>>> 0a4ed7c (add signup usecase)
>>>>>>> 14787a1 (add signup usecase)
}