package com.example.runningapp.presentation.userprofile

import android.net.Uri
import android.widget.ImageButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.runningapp.R
import com.example.runningapp.ui.composables.PrimaryButton


@Composable
fun UserProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToEditUserProfileScreen: () -> Unit,
    onNavigateToUserProfileScreen: () -> Unit,
    viewModel: UserProfileViewModel = hiltViewModel<UserProfileViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState(UserProfileScreenUiState())
    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        result.value = it
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .padding(start = 40.dp, end = 40.dp, bottom = 10.dp)
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = uiState.radialGradientBrush)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Thông tin cá nhân",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    launcher.launch(arrayOf("image/*"))
                    uiState.isUpdatingAvatar = true
                }, contentPadding = PaddingValues(0.dp)
            ) {
                AsyncImage(
                    model = uiState.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }
//            Column(
//                modifier = Modifier.padding(horizontal = 40.dp)
//            ) {
//                PrimaryButton(onClick = {
//                    launcher.launch(arrayOf("image/*"))
//                    uiState.isUpdatingAvatar = true
//                }) {
//                    Text(text = "Thay đổi ảnh đại diện")
//                }
//            }
            val context = LocalContext.current
            result.value?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.use { input ->
                    val imageBytes = input.readBytes()
                    LaunchedEffect(Unit) {
                        viewModel.updateAvatar(imageBytes)
                    }
                }
                result.value = null
            }
            Text(
                text = uiState.nickname,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "THÀNH TÍCH",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .size(100.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "%.1f".format(uiState.totalDistance),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "m",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(1.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .size(100.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${uiState.totalSteps}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "steps",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(1.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .size(100.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "%.1f".format(uiState.totalDistance / 1000 * 100 / 1.6),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "kcal",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Tôi là người năng động, thích khám phá. Hãy kết bạn với tôi và chúng ta cùng chạy nhé!!!!!!",
                    modifier = Modifier.padding(10.dp)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 10.dp)
            ) {
                PrimaryButton(
                    onClick = onNavigateToEditUserProfileScreen
                ) {
                    Text(text = "Chỉnh sửa thông tin cá nhân")
                }
            }
        }
    }
}