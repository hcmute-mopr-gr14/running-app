package com.example.runningapp.data.remote

object ApiRoutes {
    private const val BASE_URL = "http://10.0.2.2:3000/api/v1"
    const val LOGIN = "$BASE_URL/login"
    const val SIGN_UP = "$BASE_URL/signup"
    const val HOME = "$BASE_URL/home"
}
