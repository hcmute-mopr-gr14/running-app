package com.example.runningapp.presentation

sealed class Screen(val route: String) {
    object Login: Screen(route = "login")
}