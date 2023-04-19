package com.example.runningapp.domain.use_cases

import android.util.Patterns
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.LoginRequest
import com.example.runningapp.data.remote.dto.user.LoginResponseData
import com.example.runningapp.data.remote.dto.user.SignupRequest
import com.example.runningapp.data.remote.dto.user.SignupResponseData
import com.example.runningapp.data.remote.repositories.UserRepository
import com.example.runningapp.domain.models.Validation
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignupUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun signup(email: String, password: String, confirmpassword: String): ApiResponse<SignupResponseData>? {
        return userRepository.signup(SignupRequest(email = email, password = password, confirmpassword = confirmpassword))
    }

    fun validateEmail(email: String): Validation {
        if (email.isEmpty()) {
            return Validation.Error(message = "Vui lòng nhập email")
        }
        if (email.any {it.isWhitespace()}) {
            return Validation.Error(message = "Email không được có khoảng trắng")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Validation.Error(message = "Email không hợp lệ, vui lòng kiểm tra lại!")
        }
        return Validation.Success
    }

    fun validatePassword(password: String): Validation {
        val digitPattern = Pattern.compile(".*\\d.*")
        val uppercasePattern = Pattern.compile(".*[A-Z].*")

        if (password.isEmpty()) {
            return Validation.Error(message = "Vui lòng nhập mật khẩu")
        }
        if (password.any {it.isWhitespace()}) {
            return Validation.Error(message = "Mật khẩu không được có khoảng trắng")
        }
        if (password.length !in 8..32) {
            return Validation.Error(message = "Mật khẩu phải có độ dài từ 8 đến 32 ký tự")
        }
        if (!digitPattern.matcher(password).matches()) {
            return Validation.Error(message = "Mật khẩu phải chứa ít nhất một chữ số")
        }
        if (!uppercasePattern.matcher(password).matches()) {
            return Validation.Error(message = "Mật khẩu phải chứa ít nhất một chữ hoa")
        }
        return Validation.Success
    }

    fun validateConfirmpassword(confirmpassword: String, password: String): Validation {
        if (confirmpassword != password) {
            return Validation.Error(message = "Xác nhận mật khẩu không khớp")
        }
        return Validation.Success
    }
}