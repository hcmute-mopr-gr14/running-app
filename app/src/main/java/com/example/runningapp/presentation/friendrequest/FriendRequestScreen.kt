package com.example.runningapp.presentation.friendrequest

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.runningapp.data.models.IncomingFriend
import com.example.runningapp.ui.composables.MainNavigationBar

@Composable
fun FriendRequestScreen(
    viewModel: FriendRequestViewModel,
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onNavigateToProfileScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FriendRequestScreen(
        friends = uiState.friends,
        navController = navController,
        onNavigateBack = onNavigateBack,
        onClick = { friend ->
            onNavigateToProfileScreen(friend._id.toHexString())
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestScreen(
    friends: List<IncomingFriend>,
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onClick: (IncomingFriend) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Friend requests")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Go back")
                    }
                },
            )
        },
        bottomBar = { MainNavigationBar(navController) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(friends) { friend ->
                FriendRequestRow(
                    friend = friend,
                    onClick = { onClick(friend) })
            }
        }
    }
}