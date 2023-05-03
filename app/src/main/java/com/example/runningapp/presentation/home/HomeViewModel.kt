package com.example.runningapp.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.RunningLogsDataDTO
import com.example.runningapp.domain.use_cases.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUiState(
    val nickname: String = "",
    val level: Int = 0,
    val runningLogs: List<RunningLogsDataDTO> = emptyList(),
    var showAllHistoryInfo: Boolean = false,
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

sealed class HomeScreenUiEvent() {
    object HomeSuccess : HomeScreenUiEvent()
    data class HomeFailure(val error: ApiError) : HomeScreenUiEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    private val _uiEvent = Channel<HomeScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun updateShowAllHistoryInfo(showAll: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(showAllHistoryInfo = showAll)
        }
    }

    private fun calculateLevel(totalSteps: Int): Int {
        val milestones = listOf(500, 1000, 2000, 4000, 8000, 16000, 32000)
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

    fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    fun home() {
        viewModelScope.launch {
            when (val response = homeUseCase.home()) {
                is ApiResponse.Data -> {
                    val homeResponseData = response.data
                    val totalSteps = homeResponseData.runningLogs.sumOf { it.steps }
                    val level = calculateLevel(totalSteps)

                    _uiState.update { uiState ->
                        uiState.copy(
                            nickname = homeResponseData.nickname,
                            runningLogs = homeResponseData.runningLogs,
                            level = level,
                        )
                    }
                    _uiEvent.send(HomeScreenUiEvent.HomeSuccess)
                }
                is ApiResponse.Error -> {
                    _uiEvent.send(HomeScreenUiEvent.HomeFailure(response.error))
                }
                else -> {
                    _uiEvent.send(
                        HomeScreenUiEvent.HomeFailure(
                            ApiError(code = "EXCEPTION", message = "Request failed")
                        )
                    )
                }
            }
        }
    }
}