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
                        "email" to email,
                        "isAdmin" to false // default non-admin
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
        onResult: (Boolean /* isAdmin */) -> Unit,
        onFailure: (Exception?) -> Unit = {}
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { doc ->
                        val isAdmin = doc.getBoolean("isAdmin") ?: false
                        val name = doc.getString("fullName") ?: "User"
                        Toast.makeText(context, "Welcome $name!", Toast.LENGTH_SHORT).show()
                        Log.d("LOGIN_FLOW", "Login successful. isAdmin: $isAdmin")
                        onResult(isAdmin)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to get user info.", Toast.LENGTH_SHORT).show()
                        onFailure(it)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Login failed: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e("LOGIN_FLOW", "Login failed", it)
                onFailure(it)
            }
    }

    fun checkIfAdmin(onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid
        Log.d("AUTH_UID", "Checking admin for UID: $uid")  // ← This logs the UID

        if (uid == null) {
            onResult(false)
            return
        }

        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val isAdmin = document.getBoolean("isAdmin") ?: false
                Log.d("ADMIN_CHECK", "isAdmin: $isAdmin for UID: $uid")
                onResult(isAdmin)
            }
            .addOnFailureListener { exception ->
                Log.e("ADMIN_CHECK", "Error checking admin status", exception)
                onResult(false)
            }
    }

}
