package com.example.mainapplicationproject.repository


import android.net.Uri
import android.util.Log
import com.example.mainapplicationproject.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

object ProductRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

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

    fun uploadProductWithImage(
        name: String,
        price: Double,
        category: String,
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val imageRef = storage.reference.child("product_images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                imageRef.downloadUrl
            }.addOnSuccessListener { downloadUrl ->
                val product = Product(
                    name = name,
                    price = price,
                    category = category,
                    imageUrl = downloadUrl.toString()
                )
                db.collection("products")
                    .add(product)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
