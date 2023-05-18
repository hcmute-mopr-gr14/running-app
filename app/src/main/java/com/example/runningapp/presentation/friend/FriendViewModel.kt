package com.example.runningapp.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.domain.models.ValidationInput
import com.example.runningapp.domain.use_cases.FriendUseCase
import com.example.runningapp.presentation.login.LoginScreenUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendScreenUiState(
    val friends: List<Friend>,
    val dialogUiState: AddFriendDialogUiState?,
)

data class AddFriendDialogUiState(
    val emailInput: ValidationInput,
)

sealed class FriendScreenUiEvent() {
    data class AddFriendFailure(val error: ApiError) : FriendScreenUiEvent()
}

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendScreenUiState(friends = listOf(), dialogUiState = null))
    private val _uiEvent = Channel<FriendScreenUiEvent>()

    val uiState = _uiState.asStateFlow()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            friendUseCase.getFriends().distinctUntilChanged().collect { friends ->
                _uiState.update { it.copy(friends = friends) }
            }
        }
    }

    fun showAddFriendDialog() {
        _uiState.update {
            it.copy(
                dialogUiState = AddFriendDialogUiState(
                    emailInput = ValidationInput(),
                )
            )
        }
    }

    fun updateDialogEmail(email: String) {
        val dialogUiState = _uiState.value.dialogUiState ?: return
        _uiState.update {
            it.copy(
                dialogUiState = dialogUiState.copy(
                    emailInput = dialogUiState.emailInput.copy(
                        value = email
                    )
                )
            )
        }
    }

    fun confirmAddFriendDialog() {
        val dialogUiState = _uiState.value.dialogUiState ?: return
        _uiState.update {
            it.copy(
                dialogUiState = dialogUiState.copy(
                    emailInput = dialogUiState.emailInput.copy(
                        validation = friendUseCase.validateEmail(
                            dialogUiState.emailInput.value
                        )
                    ),
                )
            )
        }
        if (dialogUiState.emailInput.validation is Validation.Error) {
            return
        }

        viewModelScope.launch {
            when (val response = friendUseCase.sendFriendRequest(dialogUiState.emailInput.value)) {
                is ApiResponse.Data -> {
                    _uiState.update { it.copy(dialogUiState = null) }
                }

                is ApiResponse.Error -> {
                    _uiEvent.send(FriendScreenUiEvent.AddFriendFailure(error = response.error))
                }
            }
        }
    }

    fun dismissAddFriendDialog() {
        _uiState.update { it.copy(dialogUiState = null) }
    }
}
