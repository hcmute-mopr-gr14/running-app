package com.example.runningapp.presentation

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
import com.example.runningapp.ui.theme.RunningAppTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunningAppTheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { contentPadding ->
                        Column(modifier = Modifier.padding(contentPadding)) {
                            AppNavGraph(navController, snackbarHostState)
                        }
                    }
                }
            }
        }
    }
}
