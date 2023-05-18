package com.example.runningapp.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.models.Friend
import com.example.runningapp.domain.use_cases.FriendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendScreenUiState(
    val friends: List<Friend>
)

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendScreenUiState(friends = listOf()))

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            friendUseCase.getFriends().distinctUntilChanged().collect { friends ->
                _uiState.update { it.copy(friends = friends) }
            }
        }
    }
}
