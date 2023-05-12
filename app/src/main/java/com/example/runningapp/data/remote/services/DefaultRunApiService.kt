package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.ApiRoutes
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.ApiResponseDTO
import com.example.runningapp.data.remote.dto.run.AddRoundRequestDTO
import com.example.runningapp.data.remote.dto.run.AddRoundResponseDataDTO
import com.example.runningapp.data.remote.dto.run.RunDTO
import com.example.runningapp.di.IoDispatcher
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRunApiService @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val client: HttpClient
) : RunApiService {
    override suspend fun fetchRuns(): ApiResponse<List<RunDTO>> {
        return withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<List<RunDTO>> = client.get(ApiRoutes.USER_RUNS).body()
                dto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error("--", ApiError(code = "EXCEPTION_ERROR", e.toString()))
            }
        }
    }

    override suspend fun addRound(round: AddRoundRequestDTO): ApiResponse<AddRoundResponseDataDTO> =
        withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<AddRoundResponseDataDTO> = client.post(ApiRoutes.USER_RUNS) {
                    contentType(ContentType.Application.Json)
                    setBody(round)
                }.body()
                dto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error("--", ApiError(code = "EXCEPTION_ERROR", e.toString()))
            }
        }
}