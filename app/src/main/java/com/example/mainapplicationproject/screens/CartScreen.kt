package com.example.mainapplicationproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mainapplicationproject.data.CartItem
import com.example.mainapplicationproject.repository.CartRepository
import com.example.mainapplicationproject.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    fun loadCart() {
        userId?.let {
            CartRepository.getCartItems(it) { items ->
                cartItems = items
                loading = false
            }
        }
    }

    LaunchedEffect(userId) { loadCart() }

    val totalPrice = cartItems.sumOf { (it.price ?: 0.0) * (it.quantity ?: 1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = {
                            userId?.let {
                                CartRepository.clearCart(
                                    userId = it,
                                    onSuccess = {
                                        Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show()
                                        cartItems = emptyList()
                                    },
                                    onFailure = {
                                        Toast.makeText(context, "Failed to clear cart", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear All")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        CartItemCard(item, onDelete = {
                            userId?.let {
                                CartRepository.removeFromCart(it, item.id ?: "")
                                cartItems = cartItems.filter { it.id != item.id }
                            }
                        })
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Total: Ksh $totalPrice",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (userId != null) {
                            OrderRepository.placeOrderFromCart(
                                userId = userId,
                                cartItems = cartItems,
                                onSuccess = {
                                    Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                                    CartRepository.clearCart(
                                        userId = userId,
                                        onSuccess = { cartItems = emptyList() },
                                        onFailure = {
                                            Toast.makeText(context, "Failed to clear cart", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                },
                                onFailure = {
                                    Toast.makeText(context, "Failed to place order: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Place Order")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.productName, style = MaterialTheme.typography.titleMedium)
                Text("Name: ${item.customizationName}")
                Text("Details: ${item.customizationDetails}")
                val qty = item.quantity ?: 1
                val price = item.price ?: 0.0
                Text("Quantity: $qty × Ksh $price = Ksh ${qty * price}")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Remove item")
            }
        }
    }
}
