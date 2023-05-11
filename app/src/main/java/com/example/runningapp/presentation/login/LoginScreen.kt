package com.example.runningapp.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.runningapp.R
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.presentation.Screen
import com.example.runningapp.ui.composables.ValidationSlot
import com.example.runningapp.ui.theme.RunningAppTheme

@Composable
fun LoginScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onNavigateToOnBoarding: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(LoginScreenUiState())
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.flowWithLifecycle(lifecycle).collect { event ->
                when (event) {
                    is LoginScreenUiEvent.LoginSuccess -> {
                        // TODO: navigate to somewhere
                        navController.navigate(route = Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }

                    is LoginScreenUiEvent.LoginFailure -> {
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
    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::setEmail,
        onPasswordChange = viewModel::setPassword,
        onRememberMeChange = viewModel::setRememberMe,
        onSubmit = viewModel::login,
        onNavigateToOnBoarding = onNavigateToOnBoarding,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginScreenUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToOnBoarding: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = Modifier.fillMaxSize().padding(8.dp, 0.dp).then(modifier)) {
        IconButton(onClick = onNavigateToOnBoarding) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.login_back_description)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            modifier = Modifier.weight(1f).padding(horizontal = 32.dp)
        ) {
            val painter = painterResource(R.drawable.logo)
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.login_logo_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .aspectRatio(
                        ratio = painter.intrinsicSize.width / painter.intrinsicSize.height,
                        matchHeightConstraintsFirst = true
                    )
                    .weight(1f)
            )
            Text(
                stringResource(R.string.login_header),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            ValidationSlot(validation = uiState.emailInput.validation) {
                OutlinedTextField(
                    value = uiState.emailInput.value,
                    onValueChange = onEmailChange,
                    label = { Text(stringResource(id = R.string.login_email_label)) },
                    placeholder = { Text(stringResource(id = R.string.login_email_placeholder)) },
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.emailInput.validation is Validation.Error,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
            }
            ValidationSlot(validation = uiState.passwordInput.validation) {
                OutlinedTextField(
                    value = uiState.passwordInput.value,
                    onValueChange = onPasswordChange,
                    label = { Text(stringResource(id = R.string.login_password_label)) },
                    placeholder = { Text(stringResource(id = R.string.login_password_placeholder)) },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = uiState.passwordInput.validation is Validation.Error,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = uiState.rememberMe,
                        onCheckedChange = onRememberMeChange,
                    )
                    Text(
                        text = stringResource(id = R.string.login_remember_me),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                TextButton(onClick = {}) {
                    Text(
                        text = stringResource(id = R.string.login_forgot_password),
                        textAlign = TextAlign.End,
                        style = TextStyle(
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.primary
                        ),
                    )
                }
            }
            Button(
                onClick = onSubmit,
                contentPadding = PaddingValues(0.dp, 14.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.emailInput.value.isNotEmpty() && uiState.passwordInput.value.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.login_login_button))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.login_new_user))
                TextButton(onClick = {}, contentPadding = PaddingValues(), modifier = Modifier.padding(start = 0.dp)) {
                    Text(text = stringResource(R.string.login_sign_up), color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    RunningAppTheme() {
        Surface {
            LoginScreen(
                uiState = LoginScreenUiState(),
                onEmailChange = {},
                onPasswordChange = {},
                onRememberMeChange = {},
                onSubmit = {},
                onNavigateToOnBoarding = {})
        }
    }
}