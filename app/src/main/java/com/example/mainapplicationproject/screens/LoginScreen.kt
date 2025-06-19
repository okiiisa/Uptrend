package com.example.mainapplicationproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mainapplicationproject.loginUser

@Composable
fun LoginScreen(){
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {  password = it },
            label = { Text("Password") },
            modifier = Modifier
                .padding(top = 8.dp)
        )
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()){
                    loginUser(email, password)
                    Toast.makeText(context, "Lgging in...", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Enter email and password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ){
            Text("Login")
        }
    }
}

