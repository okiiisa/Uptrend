package com.example.mainapplicationproject.repository

import android.util.Log
import com.example.mainapplicationproject.data.CartItem
import com.example.mainapplicationproject.data.Order
import com.google.firebase.firestore.FirebaseFirestore

object OrderRepository {
    private val db = FirebaseFirestore.getInstance()

    fun fetchOrders(
        onResult: (List<Order>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("orders")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { snapshot ->
                val orders = snapshot.map { doc ->
                    doc.toObject(Order::class.java)
                }
                onResult(orders)
            }
            .addOnFailureListener { e ->
                Log.e("OrderRepo", "Error fetching orders", e)
                onError(e)
            }
    }

    fun submitOrder(
        order: Order,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("orders")
            .add(order)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                Log.e("OrderRepo", "Error submitting order", e)
                onFailure(e)
            }
    }

    fun placeOrderFromCart(
        userId: String,
        cartItems: List<CartItem>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val batch = db.batch()
        val ordersRef = db.collection("orders")
        val userCartRef = db.collection("users").document(userId).collection("cart")

        try {
            cartItems.forEach { item ->
                val order = Order(
                    userId = userId,
                    productId = item.productId,
                    productName = item.productName,
                    customizationName = item.customizationName,
                    customizationDetails = item.customizationDetails,
                    timestamp = System.currentTimeMillis()
                )

                val newOrderRef = ordersRef.document()
                batch.set(newOrderRef, order)

                // Also delete the item from the cart
                if (item.id.isNotBlank()) {
                    val cartItemRef = userCartRef.document(item.id)
                    batch.delete(cartItemRef)
                }
            }

            batch.commit()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e ->
                    Log.e("OrderRepo", "Batch failed", e)
                    onFailure(e)
                }

        } catch (e: Exception) {
            Log.e("OrderRepo", "Exception placing order", e)
            onFailure(e)
        }
    }
}
