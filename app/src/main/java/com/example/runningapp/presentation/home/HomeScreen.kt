package com.example.runningapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.runningapp.R
import com.example.runningapp.domain.utils.metersToCalories
import java.util.*
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRunningScreen: () -> Unit,
    onNavigateToUserProfileScreen: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(HomeScreenUiState())
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .padding(horizontal = 40.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush = uiState.radialGradientBrush)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiState.bottomNavigationItems.forEachIndexed { index, icon ->
                        IconButton(
                            onClick = {
                                uiState.selectedIndex = index
                                when (index) {
                                    0 -> onNavigateToHome()
                                    3 -> onNavigateToUserProfileScreen()
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (uiState.selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (!uiState.showAllHistoryInfo) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Giao diện chính
                    Column() {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .clip(
                                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                                )
                                .background(brush = uiState.radialGradientBrush)
                        ) {
                            Row(Modifier.padding(top = 20.dp)) {
                                IconButton(
                                    onClick = onNavigateToUserProfileScreen,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .padding(start = 10.dp, end = 10.dp)
                                ) {
                                    AsyncImage(
                                        model = uiState.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .fillMaxSize()
                                    )
                                }
                                Column() {
                                    Text(
                                        text = "HELLO!",
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = uiState.nickname,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(170.dp))
                                IconButton(onClick = { }) {
                                    Image(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = ""
                                    )
                                }
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column() {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${uiState.remainingSteps}",
                                                fontSize = 16.sp,
                                                modifier = Modifier
                                                    .alignByBaseline()
                                                    .align(Alignment.CenterVertically)
                                            )
                                            Text(
                                                text = "/${uiState.nextMilestone}",
                                                fontSize = 20.sp,
                                                modifier = Modifier
                                                    .alignByBaseline()
                                                    .align(Alignment.CenterVertically)
                                            )
                                            Text(
                                                text = " steps",
                                                modifier = Modifier
                                                    .alignByBaseline()
                                                    .align(Alignment.CenterVertically)
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "Level ${uiState.level}",
                                            color = Color.Yellow,
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )
                                    }
                                    AccumulatedValueRainbowBar(accumulatedValue = uiState.remainingSteps.toFloat() / uiState.nextMilestone)
                                }
                                /*Spacer(modifier = Modifier.width(10.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "",
                                    Modifier.size(40.dp)
                                )*/
                            }
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxWidth()
                                    .height(190.dp)
                                    .padding(horizontal = 10.dp, vertical = 20.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = ""
                                )
                                /*Column() {
                                    Text(text = "title")
                                    Text(text = "information")
                                }*/
                            }
                            Button(
                                onClick = onNavigateToRunningScreen,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "status"
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(start = 20.dp)
                                ) {
                                    Text(
                                        text = "Today",
                                        color = Color(red = 67, green = 196, blue = 101),
                                        fontSize = 14.sp
                                    )
                                    Text(text = "01 : 09 : 44 ", fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(60.dp))
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    CircularProgressIndicator(
                                        progress = 1f,
                                        color = Color(red = 241, green = 73, blue = 133),
                                        strokeWidth = 5.dp,
                                        modifier = Modifier
                                            .rotate(270f)
                                            .matchParentSize()
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_walk),
                                            contentDescription = "Walk Icon",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(end = 10.dp)
                                        ) {
                                            Text(
                                                text = "2345",
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center,
                                                fontSize = 10.sp
                                            )
                                            Text(
                                                text = "───",
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = "5000",
                                                style = MaterialTheme.typography.bodySmall,
                                                textAlign = TextAlign.Center,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "History",
                                Modifier.padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.width(220.dp))
                            TextButton(onClick = { viewModel.updateShowAllHistoryInfo(true) }) {
                                Text(text = "See All")
                            }
                        }
                        HistoryInfo()
                    }
                }
            }
            if (uiState.showAllHistoryInfo) {
                Box(modifier = Modifier.fillMaxSize()) {
                    HistoryInfo()
                    IconButton(
                        onClick = { viewModel.updateShowAllHistoryInfo(false) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            }
        }
    }
}


@Composable
fun AccumulatedValueRainbowBar(accumulatedValue: Float) {
    val rainbowBrush = Brush.linearGradient(
        colors = listOf(
            Color.Red,
            Color(0xFFFF7F00), // Orange
            Color.Yellow,
            Color.Green,
            Color.Blue,
            Color(0xFF4B0082), // Indigo
            Color.Magenta
        )
    )
    // Thanh nền màu sắc cầu vồng
    Box(
        modifier = Modifier
            .height(10.dp)
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        // Thanh tiến trình theo số tích lũy
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(accumulatedValue)
                .height(10.dp)
                .background(brush = rainbowBrush)
                .background(Color.White.copy(alpha = 0.7f))
        )
    }
}

@Composable
fun HistoryInfo(
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState(HomeScreenUiState())
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .zIndex(1f)
    ) {
        items(uiState.runs) { run ->
            val totalMeters = run.rounds.sumOf { it.meters }
            val totalCalories = run.rounds.sumOf { metersToCalories(it.meters, it.seconds) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = run.date.toString(),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "%.1fm  %.0f kcal".format(
                                totalMeters,
                                totalCalories
                            ),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(130.dp))
                    Text(
                        text = String.format(
                            Locale.ENGLISH,
                            "%d steps",
                            (totalMeters / 1000 * 1_471).roundToInt()
                        ),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}