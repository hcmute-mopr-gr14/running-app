package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*

interface UserApiService {
    suspend fun login(body: LoginRequestDTO): ApiResponse<LoginResponseDataDTO>?
    suspend fun signup(body: SignupRequestDTO): ApiResponse<SignupResponseDataDTO>?
    suspend fun fetchHomeData(): ApiResponse<HomeResponseDataDTO>?
}