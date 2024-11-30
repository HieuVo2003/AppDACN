package com.example.mohinhstore.Controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mohinhstore.BottomNavBar
import com.example.mohinhstore.Model.AuthViewModel
import com.example.mohinhstore.Model.Product
import com.example.mohinhstore.Model.User
import com.example.mohinhstore.Screen.CartScreen
import com.example.mohinhstore.Screen.CheckoutScreen
import com.example.mohinhstore.Screen.HomeScreen
import com.example.mohinhstore.Screen.LoginScreen
import com.example.mohinhstore.Screen.PaymentSuccessScreen
import com.example.mohinhstore.Screen.ProductDetailScreen
import com.example.mohinhstore.Screen.RegisterScreen
import com.example.mohinhstore.Screen.UserInfoScreen
import com.example.mohinhstore.ui.theme.MoHinhStoreTheme
//import com.google.android.gms.analytics.ecommerce.Product

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
    val cartItems = remember { mutableStateListOf<Product>() } // Giỏ hàng
    val loggedInUser = remember { mutableStateOf<User?>(null) } // Trạng thái người dùng (null nếu chưa đăng nhập)

    Scaffold(
        bottomBar = {
            if (loggedInUser.value != null) { // Chỉ hiển thị BottomNavBar khi đã đăng nhập
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (loggedInUser.value == null) "login" else "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Màn hình đăng nhập
            composable("login") {
                LoginScreen(
                    navController = navController,
                    onLoginSuccess = { user ->
                        loggedInUser.value = user // Cập nhật trạng thái người dùng
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true // Chỉ giữ lại màn hình login trên stack
                        }
                    }
                )
            }

            // Màn hình đăng ký
            composable("register") {
                RegisterScreen(
                    navController = navController,
                    onRegisterSuccess = { user ->
                        loggedInUser.value = user
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            // Màn hình chính (home)
            composable("home") {
                if (loggedInUser.value != null) {
                    HomeScreen(navController = navController, cartItems = cartItems)
                } else {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }

                    }
                }
            }

            // Màn hình giỏ hàng
            composable("cart") {
                if (loggedInUser.value != null) {
                    CartScreen(cartItems = cartItems, navController = navController)
                } else {
                    navController.navigate("login") {
                        popUpTo("cart") { inclusive = true }
                    }
                }
            }

            // Chi tiết sản phẩm
            composable(
                "productDetail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                if (loggedInUser.value != null) {
                    ProductDetailScreen(productId = productId)
                } else {
                    navController.navigate("login") {
                        popUpTo("productDetail") { inclusive = true }
                    }
                }
            }

            // Màn hình thông tin người dùng
            composable("userInfo") {
                if (loggedInUser.value != null) {
                    UserInfoScreen(
                        navController = navController,
                        onLogout = {
                            loggedInUser.value = null // Xóa trạng thái đăng nhập
                            cartItems.clear() // Xóa giỏ hàng
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true } // Xóa stack tới màn hình home
                            }
                        }
                    )
                } else {
                    navController.navigate("login") {
                        popUpTo("userInfo") { inclusive = true }
                    }
                }
            }

            // Thanh toán
            composable("checkout") {
                if (loggedInUser.value != null) {
                    CheckoutScreen(cartItems = cartItems, navController = navController)
                } else {
                    navController.navigate("login") {
                        popUpTo("checkout") { inclusive = true }
                    }
                }
            }

            // Màn hình thanh toán thành công
            composable("success") {
                if (loggedInUser.value != null) {
                    PaymentSuccessScreen(
                        navController = navController,
                        onClearCart = { cartItems.clear() } // Xóa giỏ hàng sau khi thanh toán
                    )
                } else {
                    navController.navigate("login") {
                        popUpTo("success") { inclusive = true }
                    }
                }
            }
        }
    }
}




