package com.example.runningapp.data.remote

object ApiRoutes {
    private const val BASE_URL = "http://10.0.2.2:3000/api/v1"
    const val LOGIN = "$BASE_URL/login"
    const val SIGN_UP = "$BASE_URL/signup"
    const val USER = "$BASE_URL/user"
    const val USER_RUNS = "$USER/runs"
    const val USER_FRIENDS = "$USER/friends"
    const val USER_FRIENDS_REQUESTS = "$USER_FRIENDS/requests"
    const val USER_FRIENDS_REQUESTS_INCOMING = "$USER_FRIENDS/requests/incoming"
}
