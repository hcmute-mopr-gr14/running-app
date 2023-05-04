package com.example.runningapp.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.runningapp.R
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.presentation.Screen
import com.example.runningapp.ui.composables.PrimaryButton
import com.example.runningapp.ui.composables.SecondaryButton
import com.example.runningapp.ui.composables.ValidationSlot

@Composable
fun SignupScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState(SignupScreenState())
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    var isPasswordOpen by remember { mutableStateOf(false) }
    var isConfirmPasswordOpen by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is SignupScreenUiEvent.SignupSuccess -> {
                        snackbarHostState.showSnackbar(
                            message = "Đăng ký tài khoản thành công",
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                        navController.navigate(route = Screen.Login.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
                    }

                    is SignupScreenUiEvent.SignupFailure -> {
                        snackbarHostState.showSnackbar(
                            message = "Email đã được sử dụng",
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 0.dp)
            .then(modifier)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
                    .size(130.dp)
            )
            Text(
                stringResource(R.string.signup_header),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            ValidationSlot(validation = uiState.emailInput.validation) {
                OutlinedTextField(
                    value = uiState.emailInput.value,
                    onValueChange = { value -> viewModel.setEmail(value) },
                    label = {
                        Text(
                            stringResource(id = R.string.signup_email_label),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.emailInput.validation is Validation.Error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    singleLine = true,
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(24.dp)
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            ValidationSlot(validation = uiState.passwordInput.validation) {
                OutlinedTextField(
                    value = uiState.passwordInput.value,
                    onValueChange = { value -> viewModel.setPassword(value) },
                    label = {
                        Text(
                            stringResource(id = R.string.signup_password_label),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.passwordInput.validation is Validation.Error,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(24.dp)
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    visualTransformation = if (!isPasswordOpen) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { isPasswordOpen = !isPasswordOpen }) {
                            if (!isPasswordOpen) {
                                Icon(
                                    painter = painterResource(id = R.drawable.eye_open),
                                    contentDescription = "",
                                    modifier = Modifier.size(26.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.eye_close),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }
                )
            }
            ValidationSlot(validation = uiState.confirm_passwordInput.validation) {
                OutlinedTextField(
                    value = uiState.confirm_passwordInput.value,
                    onValueChange = { value -> viewModel.setConfirmpassword(value) },
                    label = {
                        Text(
                            stringResource(id = R.string.signup_confirmpassword_label),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.confirm_passwordInput.validation is Validation.Error,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(24.dp)
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    visualTransformation = if (!isConfirmPasswordOpen) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { isConfirmPasswordOpen = !isConfirmPasswordOpen }) {
                            if (!isConfirmPasswordOpen) {
                                Icon(
                                    painter = painterResource(id = R.drawable.eye_open),
                                    contentDescription = "",
                                    modifier = Modifier.size(26.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.eye_close),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }
                )
            }
            PrimaryButton(
                onClick = { viewModel.signup() }
            ) {
                Text(
                    text = stringResource(R.string.signup_signup_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            SecondaryButton(
                onClick = { navController.navigate("login") }
            ) {
                Text(
                    text = stringResource(R.string.signup_backtologin_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
