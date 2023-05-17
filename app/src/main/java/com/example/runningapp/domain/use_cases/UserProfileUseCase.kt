package com.example.runningapp.domain.use_cases

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.ChangepasswordResponseDataDTO
import com.example.runningapp.data.remote.dto.user.UserRequestDTO
import com.example.runningapp.data.remote.dto.user.UserResponseDataDTO
import com.example.runningapp.data.remote.services.UserApiService
import com.example.runningapp.data.repositories.UserRepository
import com.example.runningapp.domain.models.Validation
import java.util.regex.Pattern
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(private val userRepository: UserRepository, private val userApiService: UserApiService) {
    suspend fun getUser() = userRepository.getUser()

    suspend fun updateAvatarOnApi(imageData: ByteArray): ApiResponse<UserResponseDataDTO>? {
        return userApiService.updateAvatar(imageData)
    }

    suspend fun changePassword(nickname: String?, currentPassword: String, newPassword: String): ApiResponse<ChangepasswordResponseDataDTO>? {
        return userApiService.changePassword(UserRequestDTO(nickname = nickname, currentPassword = currentPassword, newPassword = newPassword))
    }

    fun validateNickname(nickname: String?): Validation {
        if (nickname == null || nickname.isEmpty()) {
            return Validation.Success
        }
        if (nickname.any { it.isWhitespace() }) {
            return Validation.Error(message = "Biệt danh không được có khoảng trắng")
        }
        return Validation.Success
    }

    fun validateCurrentPassword(currentPassword: String): Validation {
        if (currentPassword.isEmpty()) {
            return Validation.Error(message = "Vui lòng nhập mật khẩu hiện tại")
        }
        return Validation.Success
    }

    fun validateNewPassword(newPassword: String): Validation {
        val digitPattern = Pattern.compile(".*\\d.*")
        val uppercasePattern = Pattern.compile(".*[A-Z].*")

        if (newPassword.isEmpty()) {
            return Validation.Error(message = "Vui lòng nhập mật khẩu mới")
        }
        if (newPassword.any {it.isWhitespace()}) {
            return Validation.Error(message = "Mật khẩu không được có khoảng trắng")
        }
        if (newPassword.length !in 8..32) {
            return Validation.Error(message = "Mật khẩu phải có độ dài từ 8 đến 32 ký tự")
        }
        if (!digitPattern.matcher(newPassword).matches()) {
            return Validation.Error(message = "Mật khẩu phải chứa ít nhất một chữ số")
        }
        if (!uppercasePattern.matcher(newPassword).matches()) {
            return Validation.Error(message = "Mật khẩu phải chứa ít nhất một chữ hoa")
        }
        return Validation.Success
    }
}