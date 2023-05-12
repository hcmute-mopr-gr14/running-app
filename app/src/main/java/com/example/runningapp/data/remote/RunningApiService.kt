package com.example.runningapp.data.remote

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*

interface RunningApiService {
    suspend fun login(body: LoginRequestDTO) : ApiResponse<LoginResponseDataDTO>?
    suspend fun signup(body: SignupRequestDTO) : ApiResponse<SignupResponseDataDTO>?
    //suspend fun fetchHomeData() : ApiResponse<UserResponseDataDTO>?
    suspend fun fetchRuns() : ApiResponse<List<RunResponseDataDTO>>?
    suspend fun fetchUser() : ApiResponse<List<UserResponseDataDTO>>?
    suspend fun updateAvatar(imageBytes: ByteArray) : ApiResponse<UserResponseDataDTO>?
}