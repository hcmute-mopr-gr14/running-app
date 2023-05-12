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
    val remainingSteps: Int = 0,
    val nextMilestone: Int = 500,
    val totalDistance: Long = 0,
    val imageUrl: String = "",
    var isUpdatingAvatar: Boolean = false,
    var selectedIndex: Int = 0,
    val bottomNavigationItems: List<ImageVector> = listOf(
        Icons.Filled.Home,
        Icons.Filled.Favorite,
        Icons.Filled.Add,
        Icons.Filled.Person
    ),
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
class UserProfileViewModel @Inject constructor(private val userprofileUseCase: UserProfileUseCase, private val homeUseCase: HomeUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileScreenUiState())
    private val _uiEvent = Channel<UserProfileScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _isUpdatingAvatar = MutableStateFlow(false)
    val isUpdatingAvatar = _isUpdatingAvatar.asStateFlow()

    init {
        viewModelScope.launch {
            homeUseCase.getRuns().collect { runs ->
                val totalMeters = runs.sumOf { it.rounds.sumOf { it.meters } }
                val totalSteps = (totalMeters / 1000 * 1_471).roundToInt()
                val (remainingSteps, nextMilestone) = calculateRemainingStepsAndNextMilestone(totalSteps)
                val level = calculateLevel(totalSteps)
                _uiState.update { uiState ->
                    uiState.copy(
                        nickname = "Nickname",
                        remainingSteps = remainingSteps,
                        nextMilestone = nextMilestone
                    )
                }
            }
            /*userprofileUseCase.getUser().collect{ user ->
                val imageUrl = user
            }*/
        }
    }

    private fun calculateLevel(totalSteps: Int): Int {
        val milestones = listOf(500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000)
        var level = 1

        for (milestone in milestones) {
            if (totalSteps >= milestone) {
                level++
            } else {
                break
            }
        }
        return level
    }

    private fun calculateRemainingStepsAndNextMilestone(totalSteps: Int): Pair<Int, Int> {
        val milestones = listOf(500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000)
        var remainingSteps = totalSteps
        var nextMilestone = milestones[0]

        for (milestone in milestones) {
            if (remainingSteps >= milestone) {
                remainingSteps -= milestone
                nextMilestone = milestone * 2
            } else {
                break
            }
        }
        return Pair(remainingSteps, nextMilestone)
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