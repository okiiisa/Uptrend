package com.example.mainapplicationproject.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUpUser(
        context: Context,
        email: String,
        password: String,
        fullName: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception?) -> Unit = {}
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = hashMapOf(
                        "fullName" to fullName,
                        "email" to email
                    )
                    db.collection("users").document(userId).set(user)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            onSuccess()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to save profile.", Toast.LENGTH_SHORT).show()
                            Log.e("Firebase", "Failed to store user profile", it)
                            onFailure(it)
                        }
                } else {
                    Toast.makeText(context, "Sign up failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("Firebase", "Sign up failed", task.exception)
                    onFailure(task.exception)
                }
            }
    }

    fun loginUser(
        context: Context,
        email: String,
        password: String,
        onSuccess: (String) -> Unit = {},
        onFailure: (Exception?) -> Unit = {}
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { doc ->
                        val name = doc.getString("fullName") ?: "User"
                        Toast.makeText(context, "Welcome $name!", Toast.LENGTH_SHORT).show()
                        Log.d("Firebase", "Welcome $name!")
                        onSuccess(name)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to get user info.", Toast.LENGTH_SHORT).show()
                        onFailure(it)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Login failed: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e("Firebase", "Login failed", it)
                onFailure(it)
            }
    }
}
