package com.example.runningapp.presentation.userprofile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.use_cases.HomeUseCase
import com.example.runningapp.domain.use_cases.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class UserProfileScreenUiState(
    val nickname: String = "",
    val nicknameInput: ValidationInput = ValidationInput(),
    val currentPasswordInput: ValidationInput = ValidationInput(),
    val newPasswordInput: ValidationInput = ValidationInput(),
    val remainingSteps: Int = 0,
    val nextMilestone: Int = 500,
    val totalDistance: Double = 0.0,
    val totalSteps: Int = 0,
    val imageUrl: String = "",
    var isUpdatingAvatar: Boolean = false,
    var selectedIndex: Int = 3,
    val lighterColor: Color = Color(123, 97, 255).copy(alpha = 0.8f),
    val radialGradientBrush: Brush = Brush.radialGradient(
        colors = listOf(lighterColor, lighterColor),
        radius = 50f
    )
)

sealed class UserProfileScreenUiEvent() {
    object UserProfileSuccess : UserProfileScreenUiEvent()
    data class UserProfileFailure(val error: ApiError) : UserProfileScreenUiEvent()
}

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userprofileUseCase: UserProfileUseCase,
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileScreenUiState())
    private val _uiEvent = Channel<UserProfileScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _isUpdatingAvatar = MutableStateFlow(false)
    val isUpdatingAvatar = _isUpdatingAvatar.asStateFlow()

    init {
        viewModelScope.launch {
            userprofileUseCase.getUser().collect { user ->
                homeUseCase.getRuns().collect { runs ->
                    val totalDistance = runs.sumOf { it.rounds.sumOf { it.meters } }
                    val totalSteps = (totalDistance / 1000 * 1_471).roundToInt()
                    _uiState.update { uiState ->
                        uiState.copy(
                            nickname = user.obj?.nickname ?: "User",
                            imageUrl = user.obj?.imageUrl ?: "",
                            totalDistance = totalDistance,
                            totalSteps = totalSteps
                        )
                    }
                }
            }
        }
    }

    fun onNicknameChanged(value: String) {
        _uiState.update { it.copy(nicknameInput = it.nicknameInput.copy(value = value)) }
    }

    fun onCurrentPasswordChanged(value: String) {
        _uiState.update { it.copy(currentPasswordInput = it.currentPasswordInput.copy(value = value)) }
    }

    fun onNewPasswordChanged(value: String) {
        _uiState.update { it.copy(newPasswordInput = it.newPasswordInput.copy(value = value)) }
    }

    fun changePassword() {
        _uiState.update {
            it.copy(
                nicknameInput = it.nicknameInput.copy(
                    validation = userprofileUseCase.validateNickname(
                        it.nicknameInput.value
                    )
                )
            )
        }
        if (_uiState.value.nicknameInput.validation is Validation.Error) {
            return
        }

        _uiState.update {
            it.copy(
                currentPasswordInput = it.currentPasswordInput.copy(
                    validation = userprofileUseCase.validateCurrentPassword(
                        it.currentPasswordInput.value
                    )
                )
            )
        }
        if (_uiState.value.currentPasswordInput.validation is Validation.Error) {
            return
        }

        _uiState.update {
            it.copy(
                newPasswordInput = it.newPasswordInput.copy(
                    validation = userprofileUseCase.validateNewPassword(
                        it.newPasswordInput.value
                    )
                )
            )
        }
        if (_uiState.value.newPasswordInput.validation is Validation.Error) {
            return
        }

        viewModelScope.launch {
            when (val response = userprofileUseCase.changePassword(
                nickname = _uiState.value.nicknameInput.value,
                currentPassword = _uiState.value.currentPasswordInput.value,
                newPassword = _uiState.value.newPasswordInput.value
            )) {
                is ApiResponse.Data -> {
                    _uiEvent.send(UserProfileScreenUiEvent.UserProfileSuccess)
                }

                is ApiResponse.Error -> {
                    _uiEvent.send(UserProfileScreenUiEvent.UserProfileFailure(response.error))
                }

                else -> {
                    _uiEvent.send(
                        UserProfileScreenUiEvent.UserProfileFailure(
                            ApiError(code = "EXCEPTION", message = "Request failed")
                        )
                    )
                }
            }
        }
    }

    fun updateAvatar(imageData: ByteArray) {
        viewModelScope.launch {
            _isUpdatingAvatar.value = true
            when (val response = userprofileUseCase.updateAvatarOnApi(imageData)) {
                is ApiResponse.Data -> {
                    val updatedUser = response.data
                    _uiState.update { uiState ->
                        uiState.copy(
                            imageUrl = updatedUser.imageUrl
                        )
                    }
                    _uiEvent.send(UserProfileScreenUiEvent.UserProfileSuccess)
                }

                is ApiResponse.Error -> {
                    _uiEvent.send(UserProfileScreenUiEvent.UserProfileFailure(response.error))
                }

                else -> {
                    _uiEvent.send(
                        UserProfileScreenUiEvent.UserProfileFailure(
                            ApiError(code = "EXCEPTION", message = "Request failed")
                        )
                    )
                }
            }
            _isUpdatingAvatar.value = false
        }
    }

}