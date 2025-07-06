package com.example.mainapplicationproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mainapplicationproject.repository.AuthRepository

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // ✅ Added this
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                    AuthRepository.signUpUser(
                        context,
                        email,
                        password,
                        fullName,
                        onSuccess = {
                            Toast.makeText(context, "Signup Successful!", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    )
                } else {
                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(top = 8.dp) // ✅ No need for .align() now
        ) {
            Text("Already have an account? Login")
        }
    }
}
