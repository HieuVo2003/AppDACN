package com.example.mohinhstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

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

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(navController = navController, cartItems = cartItems)
            }
            composable("cart") {
                CartScreen(cartItems = cartItems, navController = navController)
            }
            composable(
                "productDetail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                ProductDetailScreen(productId = productId)
            }
            composable("userInfo") {
                UserInfoScreen(navController = navController)
            }
            composable("checkout") {
                CheckoutScreen(cartItems = cartItems, navController = navController)
            }
            composable("success") {
                PaymentSuccessScreen(
                    navController = navController,
                    onClearCart = { cartItems.clear() } // Xóa giỏ hàng
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem(label = "Trang chủ", route = "home", icon = R.drawable.ic_home),
        NavigationItem(label = "Giỏ hàng", route = "cart", icon = R.drawable.ic_cart),
        NavigationItem(label = "Người dùng", route = "userInfo", icon = R.drawable.ic_user)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary // Màu nền thanh điều hướng
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        tint = if (navController.currentDestination?.route == item.route)
                            MaterialTheme.colorScheme.onPrimary // Màu khi được chọn
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant // Màu mặc định
                    )
                },
                label = { Text(item.label) },
                selected = navController.currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = true, // Hiển thị nhãn ngay cả khi không được chọn
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary, // Màu biểu tượng được chọn
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

data class NavigationItem(val label: String, val route: String, val icon: Int)


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
            painter = painterResource(id = R.drawable.banner_sample),
            contentDescription = "Banner",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun HomeScreen(navController: NavHostController, cartItems: MutableList<Product>) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchTextField()
                Button(
                    onClick = { navController.navigate("cart") }, // Điều hướng tới màn hình Giỏ hàng
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "Giỏ hàng (${cartItems.size})") // Hiển thị số lượng sản phẩm trong giỏ hàng
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
                products = generateSampleProducts(),
                navController = navController,
                cartItems = cartItems
            )
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

data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val imageResId: Int,
    val description: String
)

@Composable
fun ProductGrid(
    products: List<Product>,
    navController: NavHostController,
    cartItems: MutableList<Product>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, navController = navController, cartItems = cartItems)
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    navController: NavHostController,
    cartItems: MutableList<Product>
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // Để quản lý coroutine

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
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
                Button(
                    onClick = {
                        cartItems.add(product)  // Thêm sản phẩm vào giỏ hàng
                        scope.launch {
                            snackbarHostState.showSnackbar("Đã thêm ${product.name} vào giỏ hàng")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Thêm vào giỏ hàng")
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


//PRODUCT
@Composable
fun ProductDetailScreen(productId: Int) {
    val product = getProductById(productId)
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


//CART
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartItems: MutableList<Product>, navController: NavHostController) {
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
            if (cartItems.isNotEmpty()) {
                Button(
                    onClick = { navController.navigate("checkout") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Thanh Toán (${cartItems.size} sản phẩm)")
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
                    .padding(paddingValues)
            ) {
                items(cartItems) { product ->
                    CartItem(product)
                }
            }
        }
    }
}

@Composable
fun CartItem(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = product.name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = product.price,
            color = Color.Red,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}


//CHECKOUT
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(cartItems: MutableList<Product>, navController: NavHostController) {
    val totalAmount = cartItems.sumOf { it.price.replace(",", "").replace(" VND", "").toInt() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh Toán") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Chi tiết thanh toán", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                items(cartItems) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = product.name, modifier = Modifier.weight(1f))
                        Text(text = product.price, color = Color.Red)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tổng cộng: ${totalAmount.formatAsCurrency()} VND",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("success") {
                        popUpTo("checkout") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Hoàn tất thanh toán")
            }
        }
    }
}

fun Int.formatAsCurrency(): String {
    val formatter = java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN"))
    return formatter.format(this)
}


//PAYMENT
@Composable
fun PaymentSuccessScreen(navController: NavHostController, onClearCart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thanh toán thành công!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Green,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onClearCart() // Gọi hàm xóa giỏ hàng
            navController.navigate("home") // Điều hướng về Trang Chủ
        }) {
            Text(text = "Quay về Trang Chủ")
        }
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


//USER INFO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông Tin Người Dùng") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back), // Thay bằng icon back thực tế
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ảnh đại diện
            Image(
                painter = painterResource(id = R.drawable.ic_user_avatar), // Thay bằng ảnh đại diện thực tế
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            // Tên người dùng
            Text(
                text = "Tên Người Dùng: John Doe",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Email
            Text(
                text = "Email: johndoe@example.com",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Nút chỉnh sửa thông tin
            Button(
                onClick = { /* Xử lý chỉnh sửa thông tin */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Chỉnh sửa thông tin")
            }
        }
    }
}
