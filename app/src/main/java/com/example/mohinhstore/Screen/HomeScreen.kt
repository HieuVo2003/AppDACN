package com.example.mohinhstore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mohinhstore.Banner
import com.example.mohinhstore.Model.Product
import com.example.mohinhstore.ProductGrid
import com.example.mohinhstore.SearchTextField
import com.example.mohinhstore.generateSampleProducts

@Composable
fun HomeScreen(navController: NavHostController, cartItems: SnapshotStateList<Product>) {
    val allProducts = generateSampleProducts()
    val (filteredProducts, setFilteredProducts) = remember { mutableStateOf(allProducts) }

    Scaffold(
        topBar = {
            Column {
                SearchTextField(products = allProducts) { results ->
                    setFilteredProducts(results)
                }
                Button(
                    onClick = { navController.navigate("cart") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Giỏ hàng (${cartItems.size})")
                }
            }
        },

        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Banner()
            ProductGrid(
                products = filteredProducts,
                navController = navController,
                cartItems = cartItems
            )
        }
    }
}
