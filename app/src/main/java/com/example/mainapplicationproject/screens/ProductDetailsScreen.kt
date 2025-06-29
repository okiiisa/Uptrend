package com.example.mainapplicationproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(navController: NavController, productId: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Product ID: $productId", style = MaterialTheme.typography.titleLarge)
            // Later you can fetch product details using productId
        }
    }
}
