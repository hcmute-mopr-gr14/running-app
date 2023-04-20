package com.example.runningapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.use_cases.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignupScreenState(
    val emailInput: ValidationInput = ValidationInput(),
    val passwordInput: ValidationInput = ValidationInput(),
    val confirm_passwordInput: ValidationInput = ValidationInput(),
)
@HiltViewModel
class SignupViewModel @Inject constructor(private val signupUseCase: SignupUseCase) : ViewModel(){
    private val _uiState = MutableStateFlow(SignupScreenState())
    val uiState = _uiState.asStateFlow()

    fun setEmail(value: String) {
        _uiState.update { it.copy(emailInput = it.emailInput.copy(value = value)) }
    }

    fun setPassword(value: String) {
        _uiState.update { it.copy(passwordInput = it.passwordInput.copy(value = value)) }
    }

    fun setConfirmpassword(value: String) {
        _uiState.update { it.copy(confirm_passwordInput = it.confirm_passwordInput.copy(value = value)) }
    }

    fun signup() {
        _uiState.update { it.copy(emailInput = it.emailInput.copy(validation = signupUseCase.validateEmail(it.emailInput.value))) }
        if (_uiState.value.emailInput.validation is Validation.Error) {
            return
        }

        _uiState.update { it.copy(passwordInput = it.passwordInput.copy(validation = signupUseCase.validatePassword(it.passwordInput.value))) }
        if (_uiState.value.passwordInput.validation is Validation.Error) {
            return
        }

        _uiState.update { it.copy(confirm_passwordInput = it.confirm_passwordInput.copy(validation = signupUseCase.validateConfirmpassword(it.confirm_passwordInput.value, it.passwordInput.value))) }
        if (_uiState.value.confirm_passwordInput.validation is Validation.Error) {
            return
        }

        viewModelScope.launch {
            when(val response = signupUseCase.signup(email = _uiState.value.emailInput.value, password = _uiState.value.passwordInput.value)) {
                is ApiResponse.Data -> {
                    println("data")
                    println(response)
                }
                is ApiResponse.Error -> {
                    println("error")
                    println(response)
                }
                else -> {
                    println("wtf")
                }
            }
        }
    }
}