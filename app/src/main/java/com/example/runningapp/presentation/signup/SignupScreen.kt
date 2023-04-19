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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runningapp.R
import com.example.runningapp.domain.models.Validation
import com.example.runningapp.ui.composables.PrimaryButton
import com.example.runningapp.ui.composables.SecondaryButton
import com.example.runningapp.ui.composables.ValidationSlot


@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = viewModel(),
) {
    var isPasswordOpen by remember { mutableStateOf(false) }
    var isConfirmPasswordOpen by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(8.dp, 0.dp).then(modifier)
        .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp)
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
                color = Color.White
            )
            ValidationSlot(validation = state.emailInput.validation) {
                OutlinedTextField(
                    value = state.emailInput.value,
                    onValueChange = { value -> viewModel.setEmail(value) },
                    label = { Text(stringResource(id = R.string.signup_email_label), color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    isError = state.emailInput.validation is Validation.Error,
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
                        color = Color.White
                    )
                )
            }
            ValidationSlot(validation = state.passwordInput.validation) {
                OutlinedTextField(
                    value = state.passwordInput.value,
                    onValueChange = { value -> viewModel.setPassword(value) },
                    label = { Text(stringResource(id = R.string.signup_password_label), color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = state.passwordInput.validation is Validation.Error,
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
                        color = Color.White
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
            ValidationSlot(validation = state.confirmpasswordInput.validation) {
                OutlinedTextField(
                    value = state.confirmpasswordInput.value,
                    onValueChange = { value -> viewModel.setConfirmpassword(value) },
                    label = { Text(stringResource(id = R.string.signup_confirmpassword_label), color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = state.confirmpasswordInput.validation is Validation.Error,
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
                        color = Color.White
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
                onClick = { viewModel.signup() },
            ) {
                Text(
                    text = stringResource(R.string.signup_signup_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            SecondaryButton(
                onClick = {
            }) {
                Text(
                    text = stringResource(R.string.signup_backtologin_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}