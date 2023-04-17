package com.example.runningapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.use_cases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenState(
    val emailInput: ValidationInput = ValidationInput(),
    val passwordInput: ValidationInput = ValidationInput(),
    val rememberMe: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    fun setEmail(value: String) {
        _state.update { it.copy(emailInput = it.emailInput.copy(value = value)) }
    }

    fun setPassword(value: String) {
        _state.update { it.copy(passwordInput = it.passwordInput.copy(value = value)) }
    }

    fun setRememberMe(rememberMe: Boolean) {
        _state.update { _state.value.copy(rememberMe = rememberMe) }
    }

    fun login() {
        _state.update { it.copy(emailInput = it.emailInput.copy(validation = loginUseCase.validateEmail(it.emailInput.value))) }
        if (_state.value.emailInput.validation is Validation.Error) {
            return
        }

        _state.update { it.copy(passwordInput = it.passwordInput.copy(validation = loginUseCase.validatePassword(it.passwordInput.value))) }
        if (_state.value.passwordInput.validation is Validation.Error) {
            return
        }

        viewModelScope.launch {
            val response = loginUseCase.login(email = _state.value.emailInput.value, password = _state.value.passwordInput.value)
            println(response?.apiVersion)
            println(response?.data)
        }
    }
}
