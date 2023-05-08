package com.example.runningapp.domain.use_cases

import android.util.Patterns
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.LoginRequestDTO
import com.example.runningapp.data.remote.dto.user.LoginResponseDataDTO
import com.example.runningapp.data.repositories.UserRepository
import com.example.runningapp.domain.models.Validation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(private val apiService: RunningApiService) {
    suspend fun login(email: String, password: String): ApiResponse<LoginResponseDataDTO>? {
        return apiService.login(LoginRequestDTO(email = email, password = password))
    }

    fun validateEmail(email: String): Validation {
        if (email.isBlank()) {
            return Validation.Error(message = "Invalid email")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Validation.Error(message = "Invalid email")
        }
        return Validation.Success
    }

    fun validatePassword(password: String): Validation {
        if (password.isBlank()) {
            return Validation.Error(message = "Please enter your password")
        }
        return Validation.Success
    }
}