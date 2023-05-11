package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.run.AddRoundRequestDTO
import com.example.runningapp.data.remote.dto.run.RunDTO

interface RunApiService {
    suspend fun fetchRuns(): ApiResponse<List<RunDTO>>
    suspend fun addRound(round: AddRoundRequestDTO): ApiResponse<RunDTO>
}