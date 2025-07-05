package com.example.mainapplicationproject.repository


import com.example.mainapplicationproject.data.CartItem
import com.google.firebase.firestore.FirebaseFirestore

object CartRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addToCart(userId: String, cartItem: CartItem) {
        db.collection("users").document(userId).collection("cart").add(cartItem)
    }

    fun removeFromCart(userId: String, cartItemId: String) {
        db.collection("users").document(userId).collection("cart").document(cartItemId).delete()
    }

    fun getCartItems(userId: String, onResult: (List<CartItem>) -> Unit) {
        db.collection("users").document(userId).collection("cart").get()
            .addOnSuccessListener { snapshot ->
                val items = snapshot.map { it.toObject(CartItem::class.java).copy(id = it.id) }
                onResult(items)
            }
    }
    fun clearCart(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users").document(userId).collection("cart").get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()
                for (doc in snapshot.documents) {
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

}
