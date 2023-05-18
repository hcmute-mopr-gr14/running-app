package com.example.runningapp.presentation.friendrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.models.IncomingFriend
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.domain.use_cases.FriendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendRequestScreenUiState(
    val friends: List<IncomingFriend>,
)

sealed class FriendRequestScreenUiEvent() {
    data class AddFriendFailure(val error: ApiError) : FriendRequestScreenUiEvent()
}

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendRequestScreenUiState(friends = listOf()))
    private val _uiEvent = Channel<FriendRequestScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            friendUseCase.getIncomingFriends().distinctUntilChanged().collect { friends ->
                _uiState.update { it.copy(friends = friends) }
            }
        }
    }
}
