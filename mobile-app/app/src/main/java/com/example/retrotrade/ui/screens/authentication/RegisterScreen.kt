package com.example.retrotrade.ui.screens.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.retrotrade.ui.components.AppErrorDialog
import com.example.retrotrade.ui.navigation.authentication.RegisterUiState
import com.example.retrotrade.ui.navigation.authentication.RegisterViewModel

@Preview
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        LaunchedEffect(uiState) {
            when (uiState) {
                is RegisterUiState.Success -> {
                    viewModel.onBackToLogin()
                }
                is RegisterUiState.Error -> {
                    errorMessage = (uiState as RegisterUiState.Error).message
                    showErrorDialog = true
                }
                else -> {}
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.SwapHorizontalCircle,
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Join RetroTrade",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 1.sp
            )

            Text(
                text = "Start trading your collection.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Register Card Form
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                        },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF95A5A6)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        label = { Text("Username") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF95A5A6)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF95A5A6)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF95A5A6)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF95A5A6)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                        },
                        label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF95A5A6)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF95A5A6)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (password != confirmPassword) {
                                errorMessage = "Passwords do not match"
                                showErrorDialog = true
                            } else if (password.length < 6) {
                                errorMessage = "Password must be at least 6 characters"
                                showErrorDialog = true
                            } else {
                                viewModel.register(fullName, username, email, password)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = Color(0xFFBDC3C7)
                        ),
                        enabled =
                            uiState !is RegisterUiState.Loading &&
                            fullName.isNotBlank() &&
                            username.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            confirmPassword.isNotBlank(),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        if (uiState is RegisterUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text(
                                "REGISTER",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Login here",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { viewModel.onBackToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }


        if (showErrorDialog) {
            AppErrorDialog(
                title = "Registration Error",
                message = errorMessage,
                onDismiss = {
                    showErrorDialog = false
                    viewModel.resetState()
                }
            )
        }
    }
}
