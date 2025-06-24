package com.example.yourapp.repository

import android.util.Log
import com.example.yourapp.data.Product
import com.google.firebase.firestore.FirebaseFirestore

object ProductRepository {
    private val db = FirebaseFirestore.getInstance()

    fun fetchProducts(onResult: (List<Product>) -> Unit) {
        db.collection("products").get()
            .addOnSuccessListener { snapshot ->
                val products = snapshot.map { it.toObject(Product::class.java).copy(id = it.id) }
                onResult(products)
            }
            .addOnFailureListener {
                Log.e("ProductRepository", "Error fetching products", it)
            }
    }

    fun fetchProductsByCategory(category: String, onResult: (List<Product>) -> Unit) {
        db.collection("products").whereEqualTo("category", category).get()
            .addOnSuccessListener { snapshot ->
                val products = snapshot.map { it.toObject(Product::class.java).copy(id = it.id) }
                onResult(products)
            }
            .addOnFailureListener {
                Log.e("ProductRepository", "Error fetching by category", it)
            }
    }
}
