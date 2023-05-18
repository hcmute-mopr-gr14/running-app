package com.example.runningapp.presentation.friend

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.runningapp.data.models.Friend
import com.example.runningapp.ui.composables.MainNavigationBar

@Composable
fun FriendScreen(viewModel: FriendViewModel, navController: NavHostController, onClick: (Friend) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FriendScreen(friends = uiState.friends, navController = navController, onClick = onClick, onAddFriend = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(friends: List<Friend>, navController: NavHostController, onClick: (Friend) -> Unit, onAddFriend: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Friends")
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
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