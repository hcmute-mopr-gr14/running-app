package com.example.runningapp.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.runningapp.presentation.running.RunningViewModel
import com.example.runningapp.ui.theme.RunningAppTheme
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var runningViewModelFactory: RunningViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContent {
            RunningAppTheme(darkTheme = false) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { contentPadding ->
                        Column(modifier = Modifier.padding(contentPadding)) {
                            AppNavGraph(
                                activityContext = this@MainActivity,
                                navController = navController,
                                snackbarHostState = snackbarHostState,
                                runningViewModelFactory = runningViewModelFactory
                            )
                        }
                    }
                }
            }
        }
    }
}
