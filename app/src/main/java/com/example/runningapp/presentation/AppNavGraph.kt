package com.example.runningapp.presentation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.runningapp.presentation.friend.FriendScreen
import com.example.runningapp.presentation.friend.FriendViewModel
import com.example.runningapp.presentation.home.HomeScreen
import com.example.runningapp.presentation.intro.GetStarted
import com.example.runningapp.presentation.intro.OnBoarding
import com.example.runningapp.presentation.login.LoginScreen
import com.example.runningapp.presentation.profile.ProfileScreen
import com.example.runningapp.presentation.profile.ProfileViewModel
import com.example.runningapp.presentation.running.RunningScreen
import com.example.runningapp.presentation.running.RunningViewModel
import com.example.runningapp.presentation.signup.SignupScreen
import com.example.runningapp.presentation.userprofile.EditUserProfileScreen
import com.example.runningapp.presentation.userprofile.UserProfileScreen
import com.google.android.gms.location.LocationServices

@Composable
fun AppNavGraph(
    activityContext: Context,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    runningViewModelFactory: RunningViewModel.Factory
) {
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
            HomeScreen(
                navController = navController,
                onNavigateToRunningScreen = {
                    navController.navigate(route = Screen.Running.route)
                },
                onNavigateToUserProfileScreen = {
                    navController.navigate(route = Screen.UserProfile.route)
                })
        }
        composable(
            route = Screen.UserProfile.route,
        ) {
            UserProfileScreen(
                navController = navController,
                onNavigateToEditUserProfileScreen = {
                    navController.navigate(route = Screen.EditUserProfile.route)
                })
        }
        composable(
            route = Screen.EditUserProfile.route,
        ) {
            EditUserProfileScreen(
                snackbarHostState = snackbarHostState,
                navController = navController,
                onNavigateToUserProfileScreen = {
                    navController.navigate(route = Screen.UserProfile.route)
                }
            )
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
        composable(
            route = Screen.Friend.route,
        ) {
            val viewModel = hiltViewModel<FriendViewModel>()
            FriendScreen(
                viewModel = viewModel,
                navController = navController,
                onClick = { friend ->
                    navController.navigate(route = Screen.Profile.with(userId = friend._id.toHexString()))
                }
            )
        }
        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                navController = navController,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() })
        }
    }
}
