package com.example.runningapp.presentation.running

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.repositories.RunRepository
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class RunningScreenUiState(
    val pending: Boolean,
    val tracking: Boolean,
    val elapsedSeconds: Long,
    val totalDistance: Float,
    val points: StateFlow<List<LatLng>>,
    val cameraPositionState: CameraPositionState
)

@SuppressLint("MissingPermission")
class RunningViewModel @AssistedInject constructor(
    @Assisted private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val runRepository: RunRepository
) :
    ViewModel() {
    private val _points = MutableStateFlow<List<LatLng>>(listOf())
    private val _uiState = MutableStateFlow(
        RunningScreenUiState(
            pending = false,
            tracking = false,
            elapsedSeconds = 0,
            totalDistance = 0f,
            points = _points.asStateFlow(),
            cameraPositionState = CameraPositionState()
        )
    )

    val uiState = _uiState.asStateFlow()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.locations.last() ?: return
            if (!_uiState.value.tracking) {
                _uiState.update { it.copy(tracking = true, pending = false) }
                startCounting()
            }

            val last = _points.value.lastOrNull()
            _points.value += locationResult.locations.map { LatLng(it.latitude, it.longitude) }

            val results = FloatArray(2)
            if (last != null) {
                Location.distanceBetween(last.latitude, last.longitude, location.latitude, location.longitude, results)
                _uiState.update {
                    it.copy(totalDistance = it.totalDistance + results[0])
                }
            }
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    _uiState.value.cameraPositionState.animate(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                ),
                                18f,
                                40f,
                                if (last == null) _uiState.value.cameraPositionState.position.bearing - 90 else results[1],
                            )
                        ),
                    )
                } catch (_: Exception) {

                }
            }
        }
    }

    fun startTracking() {
        _points.update { listOf() }
        _uiState.update { it.copy(pending = true, elapsedSeconds = 0, totalDistance = 0f) }
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                2000
            ).setMinUpdateIntervalMillis(1000).build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun startCounting() {
        viewModelScope.launch {
            while (_uiState.value.tracking) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun stopTracking() {
        val position = _uiState.value.cameraPositionState.position
        _uiState.update { it.copy(pending = true) }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback).addOnCompleteListener {
            _uiState.update { it.copy(tracking = false, pending = false) }
        }
        viewModelScope.launch {
            _uiState.value.cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        position.target,
                        position.zoom - 2f,
                        position.tilt,
                        position.bearing + 90f
                    )
                )
            )
            runRepository.addRound(date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date, round = Run.Round().apply {
                points = _uiState.value.points.value.map {
                    Run.Round.LatLng().apply {
                        lat = it.latitude
                        lng = it.longitude
                    }
                }.toRealmList()
                meters = _uiState.value.totalDistance.toDouble()
                seconds = _uiState.value.elapsedSeconds
            })
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(fusedLocationProviderClient: FusedLocationProviderClient): RunningViewModel
    }
}