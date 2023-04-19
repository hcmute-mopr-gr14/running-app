package com.example.runningapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val confirmpasswordInput: ValidationInput = ValidationInput(),
)
@HiltViewModel
class SignupViewModel @Inject constructor(private val signupUseCase: SignupUseCase) : ViewModel(){
    private val _state = MutableStateFlow(SignupScreenState())
    val state = _state.asStateFlow()

    fun setEmail(value: String) {
        _state.update { it.copy(emailInput = it.emailInput.copy(value = value)) }
    }

    fun setPassword(value: String) {
        _state.update { it.copy(passwordInput = it.passwordInput.copy(value = value)) }
    }

    fun setConfirmpassword(value: String) {
        _state.update { it.copy(confirmpasswordInput = it.confirmpasswordInput.copy(value = value)) }
    }

    fun signup() {
        _state.update { it.copy(emailInput = it.emailInput.copy(validation = signupUseCase.validateEmail(it.emailInput.value))) }
        if (_state.value.emailInput.validation is Validation.Error) {
            return
        }

        _state.update { it.copy(passwordInput = it.passwordInput.copy(validation = signupUseCase.validatePassword(it.passwordInput.value))) }
        if (_state.value.passwordInput.validation is Validation.Error) {
            return
        }

        _state.update { it.copy(confirmpasswordInput = it.confirmpasswordInput.copy(validation = signupUseCase.validateConfirmpassword(it.confirmpasswordInput.value, it.passwordInput.value))) }
        if (_state.value.confirmpasswordInput.validation is Validation.Error) {
            return
        }

        viewModelScope.launch {
            val response = signupUseCase.signup(email = _state.value.emailInput.value, password = _state.value.passwordInput.value, confirmpassword = _state.value.confirmpasswordInput.value)
            println(response?.apiVersion)
            println(response?.data)
        }
    }
}