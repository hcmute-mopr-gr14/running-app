package com.example.runningapp.presentation

sealed class Screen(val route: String) {
    object SignUp : Screen(route = "signUp")
    object Login : Screen(route = "login")
    object Home : Screen(route = "home")
    object UserProfile : Screen(route = "userProfile")
    object EditUserProfile : Screen(route = "edit_userProfile")
    object GetStarted : Screen(route = "getStarted")
    object OnBoarding : Screen(route = "onBoarding")
    object Running : Screen(route = "running")
    object Friend : Screen(route = "friend")
    object Profile : Screen(route = "profile/$ARG_USER_ID") {
        fun with(userId: String) = route.replace(ARG_USER_ID, userId)
    }
    companion object {
        const val ARG_USER_ID = "{userId}"
    }
}
