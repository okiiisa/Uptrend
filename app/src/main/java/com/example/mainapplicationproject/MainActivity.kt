package com.example.mainapplicationproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mainapplicationproject.ui.theme.MainApplicationProjectTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MainApplicationProjectTheme {
                // You can place your UI here later
            }
        }
    }
}

// Logic only - not composable
fun signUpUser(email: String, password: String, fullName: String) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val db = FirebaseFirestore.getInstance()
                val user = hashMapOf(
                    "fullName" to fullName,
                    "email" to email
                )
                db.collection("users").document(userId!!).set(user)
                    .addOnSuccessListener {
                        Log.d("Firebase", "User profile created")
                    }
                    .addOnFailureListener {
                        Log.e("Firebase", "Failed to store user profile", it)
                    }
            } else {
                Log.e("Firebase", "Sign up failed", task.exception)
            }
        }
}

fun loginUser(email: String, password: String) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId!!).get()
                .addOnSuccessListener { doc ->
                    if (doc != null) {
                        val name = doc.getString("fullName")
                        Log.d("Firebase", "Welcome $name!")
                    }
                }
        }
        .addOnFailureListener {
            Log.e("Firebase", "Login failed", it)
        }
}
