package com.example.mohinhstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mohinhstore.ui.theme.MoHinhStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoHinhStoreTheme {
                MainNavHost()
            }
        }
    }
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(productId = productId)
        }
    }
}
@Composable
fun Banner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner_sample), // Sử dụng hình ảnh banner của bạn
            contentDescription = "Banner",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { SearchTextField() },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Banner()
            ProductGrid(products = generateSampleProducts(), navController = navController)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField() {
    TextField(
        value = "",
        onValueChange = { /* Handle search logic */ },
        placeholder = { Text("Tìm kiếm sản phẩm...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.LightGray
        )
    )
}


data class Product(val id: Int, val name: String, val price: String, val imageResId: Int, val description: String)

@Composable
fun ProductGrid(products: List<Product>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products.size) { index ->
            ProductCard(product = products[index], navController = navController)
        }
    }
}
@Composable
fun ProductCard(product: Product, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            navController.navigate("productDetail/${product.id}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = product.price,
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}
@Composable
fun ProductDetailScreen(productId: Int) {
    val product = getProductById(productId) // Bạn sẽ lấy thông tin sản phẩm từ danh sách sản phẩm
    if (product != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Text(
                text = product.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = product.price,
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = product.description,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    } else {
        Text(text = "Product not found!")
    }
}
fun generateSampleProducts(): List<Product> {
    return listOf(
        Product(1, "Gundam RX-78-2", "500,000 VND", R.drawable.gundam_sample1, "A legendary Gundam model."),
        Product(2, "Zaku II", "450,000 VND", R.drawable.gundam_sample2, "Famous Zaku II model."),
    )
}
fun getProductById(id: Int): Product? {
    val products = generateSampleProducts()
    return products.find { it.id == id }
}
