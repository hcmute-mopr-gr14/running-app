package com.example.runningapp.domain.utils

import kotlin.math.max

fun metersToCalories(meters: Double, seconds: Long): Double {
    val kmPerHour = meters / max(seconds, 1) * 3.6
    val METS = kmPerHour * 1.0375
    return METS * 50 * seconds / 3600
}