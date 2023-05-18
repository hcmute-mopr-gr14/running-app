package com.example.runningapp.presentation.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.runningapp.presentation.Screen
import com.example.runningapp.ui.composables.PrimaryButton
import com.example.runningapp.ui.composables.SecondaryButton
import com.example.runningapp.ui.composables.ValidationSlot


@Composable
fun EditUserProfileScreen(
    navController: NavHostController,
    onNavigateToHome: () -> Unit,
    onNavigateToUserProfileScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: UserProfileViewModel = hiltViewModel<UserProfileViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState(UserProfileScreenUiState())
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UserProfileScreenUiEvent.UserProfileSuccess -> {
                        snackbarHostState.showSnackbar(
                            message = "Đổi thông tin thành công",
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                        navController.navigate(route = Screen.UserProfile.route)
                    }

                    is UserProfileScreenUiEvent.UserProfileFailure -> {
                        snackbarHostState.showSnackbar(
                            message = event.error.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
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
                .background(color = MaterialTheme.colorScheme.background)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Chỉnh sửa thông tin cá nhân",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            // Thêm padding giữa các thành phần
            Spacer(modifier = Modifier.height(20.dp))

            // TextField cho Nickname
            ValidationSlot(validation = uiState.nicknameInput
                .validation) {
                OutlinedTextField(
                    value = uiState.nicknameInput.value,
                    onValueChange = { value -> viewModel.onNicknameChanged(value) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    label = {
                        Text(
                            text = "Biệt danh",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TextField cho Mật khẩu hiện tại
            ValidationSlot(validation = uiState.currentPasswordInput.validation) {
                OutlinedTextField(
                    value = uiState.currentPasswordInput.value,
                    onValueChange = { value -> viewModel.onCurrentPasswordChanged(value) },
                    shape = RoundedCornerShape(12.dp),
                    label = {
                        Text(
                            text = "Mật khẩu hiện tại",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TextField cho Mật khẩu mới
            ValidationSlot(validation = uiState.newPasswordInput.validation) {
                OutlinedTextField(
                    value = uiState.newPasswordInput.value,
                    onValueChange = { value -> viewModel.onNewPasswordChanged(value) },
                    shape = RoundedCornerShape(12.dp),
                    label = {
                        Text(
                            text = "Mật khẩu mới",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 40.dp)
            ) {
                PrimaryButton(
                    onClick = { viewModel.changePassword() }
                ) {
                    Text(
                        text = "Xác nhận chỉnh sửa",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                SecondaryButton(
                    onClick = onNavigateToUserProfileScreen
                ) {
                    Text(
                        text = "Hủy chỉnh sửa",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}