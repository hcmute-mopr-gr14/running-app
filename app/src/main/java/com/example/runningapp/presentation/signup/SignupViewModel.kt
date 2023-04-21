package com.example.runningapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.SignupResponseDataDTO
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.use_cases.SignupUseCase
import com.example.runningapp.presentation.login.LoginScreenUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignupScreenState(
    val emailInput: ValidationInput = ValidationInput(),
    val passwordInput: ValidationInput = ValidationInput(),
    val confirm_passwordInput: ValidationInput = ValidationInput(),
)

sealed class SignupScreenUiEvent() {
    object SignupSuccess : SignupScreenUiEvent()
    data class SignupFailure(val error: ApiError) : SignupScreenUiEvent()
}
@HiltViewModel
class SignupViewModel @Inject constructor(private val signupUseCase: SignupUseCase) : ViewModel(){
    private val _uiState = MutableStateFlow(SignupScreenState())
    private val _uiEvent = Channel<SignupScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

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
            when (val response = signupUseCase.signup(
                email = _uiState.value.emailInput.value,
                password = _uiState.value.passwordInput.value
            )) {
                is ApiResponse.Data -> {
                    _uiEvent.send(SignupScreenUiEvent.SignupSuccess)
                }

                is ApiResponse.Error -> {
                    _uiEvent.send(SignupScreenUiEvent.SignupFailure(response.error))
                }

                else -> {
                    _uiEvent.send(
                        SignupScreenUiEvent.SignupFailure(
                            ApiError(code = 1, message = "Request failed")
                        )
                    )
                }
            }
        }
    }
}