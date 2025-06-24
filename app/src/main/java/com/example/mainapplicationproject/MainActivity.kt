package com.example.mainapplicationproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mainapplicationproject.ui.theme.MainApplicationProjectTheme
import com.example.mainapplicationproject.screens.Navigation
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Set up Compose with Navigation
        setContent {
            MainApplicationProjectTheme {
                Navigation()
            }
        }
    }
}
