package com.example.runningapp.presentation.friend

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.runningapp.data.models.Friend
import com.example.runningapp.presentation.Screen
import com.example.runningapp.presentation.friendrequest.FriendRequestScreen
import com.example.runningapp.presentation.login.LoginScreenUiEvent
import com.example.runningapp.ui.composables.MainNavigationBar

@Composable
fun FriendScreen(
    viewModel: FriendViewModel,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    onNavigateToProfileScreen: (String) -> Unit,
    onNavigateToFriendRequestScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.flowWithLifecycle(lifecycle).collect { event ->
                when (event) {
                    is FriendScreenUiEvent.AddFriendFailure -> {
                        snackbarHostState.showSnackbar(
                            message = event.error.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
    FriendScreen(
        friends = uiState.friends,
        dialogUiState = uiState.dialogUiState,
        navController = navController,
        onClick = { onNavigateToProfileScreen(it._id.toHexString()) },
        onEmailChange = viewModel::updateDialogEmail,
        onAddFriend = viewModel::showAddFriendDialog,
        onDialogConfirm = viewModel::confirmAddFriendDialog,
        onDialogDismiss = viewModel::dismissAddFriendDialog,
        onNavigateToFriendRequestScreen = onNavigateToFriendRequestScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(
    friends: List<Friend>,
    dialogUiState: AddFriendDialogUiState?,
    navController: NavHostController,
    onClick: (Friend) -> Unit,
    onAddFriend: () -> Unit,
    onEmailChange: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogConfirm: () -> Unit,
    onNavigateToFriendRequestScreen: () -> Unit
) {
    if (dialogUiState != null) {
        AddFriendDialog(
            emailInput = dialogUiState.emailInput,
            onEmailChange = onEmailChange,
            onDismiss = onDialogDismiss,
            onConfirm = onDialogConfirm
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Friends")
                },
                actions = {
                    IconButton(onClick = onNavigateToFriendRequestScreen) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Friend requests")
                    }
                }
            )
        },
        bottomBar = { MainNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFriend,
                elevation = FloatingActionButtonDefaults.loweredElevation()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(friends) { friend ->
                FriendRow(
                    friend = friend,
                    onClick = {
                        onClick(friend)
                    })
            }
        }
    }
}