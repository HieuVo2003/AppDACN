package com.example.mohinhstore.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mohinhstore.Model.Product
import com.example.mohinhstore.R

val userCartData = mutableMapOf<String, MutableList<Product>>() // Lưu trạng thái giỏ hàng của từng người dùng
var currentUser: String? = null // Người dùng hiện tại (ID hoặc username)

val cartItems = SnapshotStateList<Product>() // Giỏ hàng hiện tại

fun loginUser(userId: String) {
    currentUser = userId
    cartItems.clear()
    cartItems.addAll(userCartData[userId] ?: mutableListOf())
}

fun logoutUser() {
    currentUser?.let { userId ->
        userCartData[userId] = cartItems.toMutableList() // Lưu trạng thái giỏ hàng của người dùng hiện tại
    }
    currentUser = null
    cartItems.clear()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartItems: SnapshotStateList<Product>, navController: NavHostController) {
    val totalPrice by remember {
        derivedStateOf {
            cartItems.sumOf { it.price.replace("[^0-9]".toRegex(), "").toInt() }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                if (cartItems.isNotEmpty()) {
                    Text(
                        text = "Tổng tiền: ${totalPrice} VND",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = { navController.navigate("checkout") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Thanh Toán (${cartItems.size} sản phẩm)")
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Giỏ hàng trống")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { product ->
                    CartItem(
                        product = product,
                        onRemove = { removeFromCart(product, cartItems) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItem(product: Product, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = product.imageResId),
            contentDescription = product.name,
            modifier = Modifier.size(64.dp)
        )
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        ) {
            Text(product.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(product.price, color = Color.Red, fontWeight = FontWeight.SemiBold)
        }
        Button(onClick = { onRemove() }) {
            Text("Xoá")
        }
    }
}

fun removeFromCart(product: Product, cartItems: SnapshotStateList<Product>) {
    cartItems.remove(product)
}


