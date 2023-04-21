package com.example.runningapp.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runningapp.presentation.login.LoginScreen
import com.example.runningapp.presentation.signup.SignupScreen

@Composable
fun AppNavGraph(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = Screen.SignUp.route) {
        composable(
            route = Screen.Login.route,
        ) {
            LoginScreen(snackbarHostState = snackbarHostState)
        }
        composable(
            route = Screen.SignUp.route,
        ) {
            SignupScreen(snackbarHostState = snackbarHostState, navController = navController)
        }
    }
}