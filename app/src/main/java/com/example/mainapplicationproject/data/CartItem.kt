package com.example.mainapplicationproject.data

data class CartItem(
    val id: String = "",
    val userId: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 1,
    val customizationName: String = "",
    val customizationDetails: String = "",
    val price: Double = 0.0
)

