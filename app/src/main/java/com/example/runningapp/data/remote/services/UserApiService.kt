package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*
import org.mongodb.kbson.ObjectId

interface UserApiService {
    suspend fun login(body: LoginRequestDTO): ApiResponse<LoginResponseDataDTO>?
    suspend fun signup(body: SignupRequestDTO): ApiResponse<SignupResponseDataDTO>?
    suspend fun changePassword(body: UserRequestDTO): ApiResponse<ChangepasswordResponseDataDTO>?

    suspend fun updateAvatar(imageBytes: ByteArray): ApiResponse<UserResponseDataDTO>
    suspend fun fetchUser(): ApiResponse<UserResponseDataDTO>
    suspend fun fetchUser(id: String): ApiResponse<UserResponseDataDTO>
}