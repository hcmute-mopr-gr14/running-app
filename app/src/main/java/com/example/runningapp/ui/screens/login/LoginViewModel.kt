package com.example.runningapp.ui.screens.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.remote.dto.user.LoginRequest
import com.example.runningapp.data.remote.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository, private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _rememberMe = MutableStateFlow(false)

    val email = _email.asStateFlow()
    val password = _password.asStateFlow()
    val rememberMe = _rememberMe.asStateFlow()

    fun setEmail(email: String) {
        _email.update { email }
    }

    fun setPassword(password: String) {
        _password.update { password }
    }

    fun setRememberMe(rememberMe: Boolean) {
        _rememberMe.update { rememberMe }
    }

    fun login() {
        viewModelScope.launch {
            val response = userRepository.login(LoginRequest(email.value, password.value))
            println(response?.apiVersion)
            println(response?.data)
        }
    }
}
