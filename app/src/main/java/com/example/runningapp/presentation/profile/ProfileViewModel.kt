package com.example.runningapp.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.models.User
import com.example.runningapp.domain.use_cases.FriendUseCase
import com.example.runningapp.domain.use_cases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

data class ProfileScreenUiState(
    val user: User
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUiState(user = User()))

    val uiState = _uiState.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("userId")
        if (id != null) {
            viewModelScope.launch {
                profileUseCase.getUser(ObjectId(id)).collect { user ->
                    if (user != null) {
                        _uiState.update { it.copy(user = user) }
                    }
                }
            }
        }
    }
}
