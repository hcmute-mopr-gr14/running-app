package com.example.runningapp.presentation.login

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.R
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.use_cases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginScreenUiState(
    val emailInput: ValidationInput = ValidationInput(),
    val passwordInput: ValidationInput = ValidationInput(),
    val rememberMe: Boolean = false,
)

sealed class LoginScreenUiEvent() {
    object LoginSuccess : LoginScreenUiEvent()
    data class LoginFailure(val error: ApiError) : LoginScreenUiEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val loginUseCase: LoginUseCase,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenUiState())
    private val _uiEvent = Channel<LoginScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setEmail(value: String) {
        _uiState.update { it.copy(emailInput = it.emailInput.copy(value = value)) }
    }

    fun setPassword(value: String) {
        _uiState.update { it.copy(passwordInput = it.passwordInput.copy(value = value)) }
    }

    fun setRememberMe(rememberMe: Boolean) {
        _uiState.update { _uiState.value.copy(rememberMe = rememberMe) }
    }

    fun login() {
        _uiState.update { it.copy(emailInput = it.emailInput.copy(validation = loginUseCase.validateEmail(it.emailInput.value))) }
        if (_uiState.value.emailInput.validation is Validation.Error) {
            return
        }

        _uiState.update { it.copy(passwordInput = it.passwordInput.copy(validation = loginUseCase.validatePassword(it.passwordInput.value))) }
        if (_uiState.value.passwordInput.validation is Validation.Error) {
            return
        }

        viewModelScope.launch {
            when (val response = loginUseCase.login(
                email = _uiState.value.emailInput.value,
                password = _uiState.value.passwordInput.value
            )) {
                is ApiResponse.Data -> {
                    dataStore.edit { it[stringPreferencesKey("userId")] = response.data._id }
                    _uiEvent.send(LoginScreenUiEvent.LoginSuccess)
                }

                is ApiResponse.Error -> {
                    val message = when (response.error.code) {
                        "EMAIL_NOT_FOUND_ERROR" -> application.getString(R.string.login_email_not_found)
                        "WRONG_PASSWORD_ERROR" -> application.getString(R.string.login_wrong_password)
                        else -> ""
                    }
                    _uiEvent.send(LoginScreenUiEvent.LoginFailure(response.error.copy(message = message)))
                }

                else -> {
                    _uiEvent.send(
                        LoginScreenUiEvent.LoginFailure(
                            ApiError(code = "EXCEPTION", message = "Request failed")
                        )
                    )
                }
            }
        }
    }
}
