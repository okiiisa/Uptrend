package com.example.mainapplicationproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.mainapplicationproject.data.Order
import com.example.mainapplicationproject.repository.OrderRepository
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminHomeScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Upload", "upload", Icons.Default.Upload),
        BottomNavItem("Orders", "orders", Icons.Default.List)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "upload"
            ) {
                composable("upload") { UploadProductScreen() }
                composable("orders") { OrdersScreen() }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun OrdersScreen() {
    val context = LocalContext.current
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        OrderRepository.fetchOrders(
            onResult = {
                orders = it
                loading = false
            },
            onError = {
                Toast.makeText(context, "Failed to load orders", Toast.LENGTH_SHORT).show()
                loading = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            orders.isEmpty() -> {
                Text(
                    text = "No orders available.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn {
                    items(orders) { order ->
                        OrderCard(order = order)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Product: ${order.productName}", style = MaterialTheme.typography.titleMedium)
            Text("User ID: ${order.userId}", style = MaterialTheme.typography.bodySmall)
            if (order.customizationName.isNotBlank()) {
                Text("Custom Name: ${order.customizationName}")
            }
            if (order.customizationDetails.isNotBlank()) {
                Text("Details: ${order.customizationDetails}")
            }
            Text(
                "Ordered at: ${
                    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(order.timestamp)
                }",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
