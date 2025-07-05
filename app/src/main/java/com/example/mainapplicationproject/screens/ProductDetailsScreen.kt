package com.example.mainapplicationproject.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mainapplicationproject.data.CartItem
import com.example.mainapplicationproject.data.Product
import com.example.mainapplicationproject.repository.CartRepository
import com.example.mainapplicationproject.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(navController: NavController, productId: String) {
    val context = LocalContext.current
    var product by remember { mutableStateOf<Product?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(productId) {
        ProductRepository.fetchProducts { productList ->
            product = productList.find { it.id == productId }
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customize Product") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            product?.let {
                ProductCustomizationForm(navController, it, Modifier.padding(padding))
            } ?: Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Product not found")
            }
        }
    }
}

@Composable
fun ProductCustomizationForm(
    navController: NavController,
    product: Product,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var customizationName by remember { mutableStateOf("") }
    var customizationDetails by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = rememberAsyncImagePainter(product.imageUrl),
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Name: ${product.name}", style = MaterialTheme.typography.titleLarge)
        Text("Category: ${product.category}")
        Text("Price: Ksh ${product.price}")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = customizationName,
            onValueChange = { customizationName = it },
            label = { Text("Name on Product (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = customizationDetails,
            onValueChange = { customizationDetails = it },
            label = { Text("Customization Details") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Quantity:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
            }
            Text(quantity.toString(), style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { quantity++ }) {
                Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (userId == null) {
                    Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (customizationDetails.isBlank()) {
                    Toast.makeText(context, "Please enter customization details", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val cartItem = CartItem(
                    userId = userId,
                    productId = product.id,
                    productName = product.name,
                    customizationName = customizationName,
                    customizationDetails = customizationDetails,
                    price = product.price,
                    quantity = quantity
                )

                CartRepository.addToCart(userId, cartItem)
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }
    }
}
