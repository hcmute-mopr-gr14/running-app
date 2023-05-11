package com.example.runningapp.presentation

sealed class Screen(val route: String) {
    object SignUp: Screen(route = "signUp")
    object Login: Screen(route = "login")
    object Home: Screen(route = "home")
    object GetStarted: Screen(route = "getStarted")
    object OnBoarding: Screen(route = "onBoarding")
    object Running: Screen(route = "running")
}