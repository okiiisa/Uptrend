package com.example.mainapplicationproject.data

data class Order(
    val userId: String = "",
    val productId: String = "",
    val productName: String = "",
    val customizationName: String = "",
    val customizationDetails: String = "",
    val timestamp: Long = System.currentTimeMillis()
)