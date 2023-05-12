package com.example.runningapp.presentation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runningapp.presentation.home.HomeScreen
import com.example.runningapp.presentation.intro.GetStarted
import com.example.runningapp.presentation.intro.OnBoarding
import com.example.runningapp.presentation.login.LoginScreen
import com.example.runningapp.presentation.running.RunningScreen
import com.example.runningapp.presentation.running.RunningViewModel
import com.example.runningapp.presentation.signup.SignupScreen
<<<<<<< Updated upstream
import com.google.android.gms.location.LocationServices

@Composable
fun AppNavGraph(
    activityContext: Context,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    runningViewModelFactory: RunningViewModel.Factory
) {
=======
import com.example.runningapp.presentation.userprofile.UserProfile

@Composable
fun AppNavGraph(navController: NavHostController, snackbarHostState: SnackbarHostState) {
>>>>>>> Stashed changes
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(
            route = Screen.Login.route,
        ) {
            LoginScreen(
                snackbarHostState = snackbarHostState,
                navController = navController,
                onNavigateToOnBoarding = {
                    navController.navigate(route = Screen.OnBoarding.route)
                },
                onNavigateToSignUp = {
                    navController.navigate(route = Screen.SignUp.route)
                },
            )
        }
        composable(
            route = Screen.SignUp.route,
        ) {
            SignupScreen(snackbarHostState = snackbarHostState, navController = navController)
        }
        composable(
            route = Screen.Home.route,
        ) {
<<<<<<< Updated upstream
            HomeScreen(onNavigateToRunningScreen = {
                navController.navigate(route = Screen.Running.route)
            })
=======
            HomeScreen(snackbarHostState = snackbarHostState, navController = navController)
        }
        composable(
            route = Screen.UserProfile.route,
        ) {
            UserProfile(snackbarHostState = snackbarHostState)
>>>>>>> Stashed changes
        }
        composable(
            route = Screen.GetStarted.route,
        ) {
            GetStarted(navController = navController)
        }
        composable(
            route = Screen.OnBoarding.route,
        ) {
            OnBoarding(navController = navController)
        }
        composable(
            route = Screen.Running.route,
        ) {
            RunningScreen(
                activityContext = activityContext,
                viewModel = runningViewModelFactory.create(
                    LocationServices.getFusedLocationProviderClient(activityContext)
                ),
                onNavigateToHome = {
                    navController.navigate(route = Screen.Home.route)
                }
            )
        }
    }
}
