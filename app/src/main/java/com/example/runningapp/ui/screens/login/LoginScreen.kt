package com.example.runningapp.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.ui.theme.RunningAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp, 0.dp).verticalScroll(rememberScrollState()).then(modifier)) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.login_back_description)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 32.dp)
        ) {
            val painter = painterResource(R.drawable.ic_launcher_background)
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.login_logo_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
//                    .weight(1f, false)
                    .aspectRatio(ratio = painter.intrinsicSize.width / painter.intrinsicSize.height)
                    .fillMaxHeight()
            )
            Text(
                stringResource(R.string.login_header),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = viewModel.email.collectAsState().value,
                onValueChange = { value -> viewModel.setEmail(value) },
                label = { Text(stringResource(id = R.string.login_email_label)) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = viewModel.password.collectAsState().value,
                onValueChange = { value -> viewModel.setPassword(value) },
                label = { Text(stringResource(id = R.string.login_password_label)) },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.rememberMe.collectAsState().value,
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
                onClick = { viewModel.login() },
                contentPadding = PaddingValues(0.dp, 14.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
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

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun DefaultPreview() {
    RunningAppTheme {
        Surface {
            LoginScreen()
        }
    }
}
