package com.example.runningapp.domain.models

sealed class Validation() {
    object Default: Validation()
    object Success: Validation()
    data class Error(val message: String) : Validation()
}