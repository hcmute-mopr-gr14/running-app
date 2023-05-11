package com.example.runningapp.presentation.running

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.runningapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.*
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.max

@Composable
@SuppressLint("MissingPermission")
fun RunningScreen(activityContext: Context, viewModel: RunningViewModel, onNavigateToHome: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val points by uiState.points.collectAsState()

    RunningScreen(
        context = activityContext,
        pending = uiState.pending,
        tracking = uiState.tracking,
        elapsedSeconds = uiState.elapsedSeconds,
        totalDistance = uiState.totalDistance,
        onToggleTracking = { if (it) viewModel.startTracking() else viewModel.stopTracking() },
        cameraPositionState = uiState.cameraPositionState,
        points = points,
        onNavigateToHome = onNavigateToHome
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RunningScreen(
    context: Context,
    pending: Boolean,
    tracking: Boolean,
    elapsedSeconds: Long,
    totalDistance: Float,
    cameraPositionState: CameraPositionState,
    onToggleTracking: (Boolean) -> Unit,
    points: List<LatLng>,
    onNavigateToHome: () -> Unit,
) {
    RunningMap(
        context = context,
        cameraPositionState = cameraPositionState,
        points = points,
        isMyLocationEnabled = tracking,
        modifier = Modifier.fillMaxSize()
    )
    var firstLoad by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onNavigateToHome, colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.login_back_description),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f, false)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(elapsedSeconds), ZoneId.systemDefault())
                val formattedElapsedTime = dateTime.format(
                    DateTimeFormatter.ofPattern(
                        "mm:ss",
                        Locale.getDefault()
                    )
                )
                Text(
                    text = formattedElapsedTime,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = { onToggleTracking(!tracking) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
                        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    ),
                    enabled = !pending,
                    contentPadding = PaddingValues(all = 12.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        this@Row.AnimatedVisibility(visible = !pending, enter = scaleIn(), exit = scaleOut()) {
                            Text(text = if (tracking) "Stop" else "Start")
                        }
                        this@Row.AnimatedVisibility(visible = pending, enter = scaleIn(), exit = scaleOut()) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                                strokeWidth = 3.dp,
                                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(visible = tracking || !firstLoad) {
                firstLoad = false
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.tertiary,
                        ),
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Meters",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "%.2f".format(
                                    animateFloatAsState(
                                        targetValue = totalDistance,
                                        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                                    ).value
                                ),
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }

                    // equation: METS * weight (kg) * duration (hours)
                    // METS = km/h * 1.0375
                    val kmPerHour by animateFloatAsState(targetValue = remember(totalDistance) {
                        totalDistance / max(
                            elapsedSeconds,
                            1
                        ) * 3.6
                    }.toFloat(), animationSpec = tween(durationMillis = 1000, easing = LinearEasing))
                    val METS = kmPerHour * 1.0375
                    val calories by animateFloatAsState(
                        targetValue = remember(totalDistance) { METS * 50 * elapsedSeconds / 3600 }.toFloat(),
                        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Calories",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "%.2f".format(calories),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.tertiary,
                        ),
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "KM/H",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "%.2f".format(kmPerHour),
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun RunningMap(
    context: Context,
    cameraPositionState: CameraPositionState,
    points: List<LatLng>,
    isMyLocationEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    LocationPermission {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.style_json
                ),
                isBuildingEnabled = false,
                isIndoorEnabled = false,
                isMyLocationEnabled = isMyLocationEnabled,
                isTrafficEnabled = false,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                rotationGesturesEnabled = false,
                scrollGesturesEnabled = false,
                scrollGesturesEnabledDuringRotateOrZoom = false,
                tiltGesturesEnabled = false
            ),
        ) {
            Polyline(
                points = points,
                color = MaterialTheme.colorScheme.primary,
                width = 10f,
                visible = true,
                startCap = RoundCap(),
                endCap = RoundCap(),
                jointType = JointType.ROUND,
                geodesic = true
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(content: @Composable () -> Unit) {
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (locationPermissionState.status.isGranted) {
        content()
    } else {
        Column {
            if (locationPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = "Location permission") },
                    text = { Text(text = "The location is important for this app. Please grant the permission.") },
                    confirmButton = {
                        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                            Text("Give permission")
                        }
                    })
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = "Location permission") },
                    text = { Text(text = "Location permission required for this feature to be available. Please grant the permission") },
                    confirmButton = {
                        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                            Text("Give permission")
                        }
                    })
            }
        }
    }
}
