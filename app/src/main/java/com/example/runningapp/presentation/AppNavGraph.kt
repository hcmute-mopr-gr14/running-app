package com.example.runningapp.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runningapp.presentation.login.LoginScreen

@Composable
fun AppNavGraph(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(
            route = Screen.Login.route,
        ) {
            LoginScreen(snackbarHostState = snackbarHostState)
        }
    }
}