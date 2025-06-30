package com.example.mainapplicationproject.data

data class CartItem(
    val id: String = "",
    val productId: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0
)
