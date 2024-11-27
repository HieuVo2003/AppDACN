package com.example.mohinhstore.Controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mohinhstore.HomeScreen
import com.example.mohinhstore.ProductDetailScreen
import com.example.mohinhstore.CartScreen
import com.example.mohinhstore.ui.theme.MoHinhStoreTheme
import com.example.mohinhstore.Product

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
    // Tạo NavController
    val navController = rememberNavController()

    // Danh sách sản phẩm trong giỏ hàng (dùng mutableStateListOf để theo dõi trạng thái)
    val cartItems = remember { mutableStateListOf<Product>() }

    // Điều hướng giữa các màn hình
    NavHost(navController = navController, startDestination = "home") {
        // Màn hình chính
        composable("home") {
            HomeScreen(navController = navController, cartItems = cartItems)
        }

        // Màn hình giỏ hàng
        composable("cart") {
            CartScreen(cartItems = cartItems, navController = navController)
        }

        // Màn hình chi tiết sản phẩm
        composable(
            "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Lấy productId từ đối số
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(productId = productId)
        }
    }
}
