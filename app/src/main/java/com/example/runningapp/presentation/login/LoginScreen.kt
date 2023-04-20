package com.example.runningapp.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.runningapp.R
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.ui.composables.ValidationSlot
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp, 0.dp).then(modifier)) {
        IconButton(onClick = {}) {
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
            val painter = painterResource(R.drawable.ic_launcher_background)
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
                    onValueChange = { value -> viewModel.setEmail(value) },
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
                    onValueChange = { value -> viewModel.setPassword(value) },
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
                        onCheckedChange = { checked ->
                            viewModel.setRememberMe(checked)
                        }
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
                onClick = {
                    coroutineScope.launch {
                        val ok = viewModel.login().await()
                        if (ok) {
                            // TODO: navigate
                        }
                    }
                },
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
