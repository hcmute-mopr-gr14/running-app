package com.example.runningapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.use_cases.LoginUseCase
import com.example.runningapp.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenUiState(
    val emailInput: ValidationInput = ValidationInput(),
    val passwordInput: ValidationInput = ValidationInput(),
    val rememberMe: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun setEmail(value: String) {
        _uiState.update { it.copy(emailInput = it.emailInput.copy(value = value)) }
    }

    fun setPassword(value: String) {
        _uiState.update { it.copy(passwordInput = it.passwordInput.copy(value = value)) }
    }

    fun setRememberMe(rememberMe: Boolean) {
        _uiState.update { _uiState.value.copy(rememberMe = rememberMe) }
    }

    fun login(): Deferred<Boolean> {
        _uiState.update { it.copy(emailInput = it.emailInput.copy(validation = loginUseCase.validateEmail(it.emailInput.value))) }
        if (_uiState.value.emailInput.validation is Validation.Error) {

            return CompletableDeferred(true)
        }

        _uiState.update { it.copy(passwordInput = it.passwordInput.copy(validation = loginUseCase.validatePassword(it.passwordInput.value))) }
        if (_uiState.value.passwordInput.validation is Validation.Error) {
            return CompletableDeferred(false)
        }

        return viewModelScope.async {
            when(val response = loginUseCase.login(email = _uiState.value.emailInput.value, password = _uiState.value.passwordInput.value)) {
                is ApiResponse.Data -> {
                    println(response)
                    return@async true
                }
                is ApiResponse.Error -> {
                    println(response)
                    return@async false
                }
                else -> {
                    return@async false
                }
            }
        }
    }
}
