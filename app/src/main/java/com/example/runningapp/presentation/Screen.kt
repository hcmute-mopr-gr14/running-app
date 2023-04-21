package com.example.runningapp.presentation

sealed class Screen(val route: String) {
    object SignUp: Screen(route = "signUp")
    object Login: Screen(route = "login")
}