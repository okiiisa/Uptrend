package com.example.yourapp.repository

import com.example.yourapp.data.Customization
import com.google.firebase.firestore.FirebaseFirestore

object CustomizationRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveCustomization(userId: String, customization: Customization) {
        db.collection("users").document(userId).collection("customizations").add(customization)
    }
}
