package com.example.mainapplicationproject.repository


import com.example.mainapplicationproject.data.Customization
import com.google.firebase.firestore.FirebaseFirestore

object CustomizationRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveCustomization(userId: String, customization: Customization) {
        db.collection("users").document(userId).collection("customizations").add(customization)
    }
}
