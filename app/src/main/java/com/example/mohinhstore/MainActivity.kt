    package com.example.mohinhstore

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Home
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material.icons.filled.ShoppingCart
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.rememberCoroutineScope
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import com.example.mohinhstore.Controller.MainNavHost
    import com.example.mohinhstore.Model.Product
    import com.example.mohinhstore.Model.User
    import com.example.mohinhstore.Screen.authenticateUser
    import com.example.mohinhstore.Screen.cartItems
    import com.example.mohinhstore.Screen.loginUser
    import com.example.mohinhstore.Screen.logoutUser
    import com.example.mohinhstore.Screen.registerUser
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
    fun BottomNavBar(navController: NavHostController) {
        val items = listOf(
            NavigationItem(label = "Trang chủ", route = "home", icon = Icons.Default.Home),
            NavigationItem(label = "Giỏ hàng", route = "cart", icon = Icons.Default.ShoppingCart),
            NavigationItem(label = "Người dùng", route = "userInfo", icon = Icons.Default.Person)
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
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
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }

    data class NavigationItem(val label: String, val route: String, val icon: ImageVector)



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


    //THANH TÌM KIẾM
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(products: List<Product>, onSearchResults: (List<Product>) -> Unit) {
    val searchText = remember { mutableStateOf("") }

    TextField(
        value = searchText.value,
        onValueChange = { query ->
            searchText.value = query
            val filteredResults = if (query.isEmpty()) {
                products
            } else {
                products.filter { it.name.contains(query, ignoreCase = true) }
            }
            onSearchResults(filteredResults)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text = "Tìm kiếm sản phẩm...") },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


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


    //XỬ LÝ LOGIC SIGNIN & SIGNUP
    // Danh sách người dùng lưu trữ trong bộ nhớ
    val userList = mutableListOf<User>()
    // Ví dụ sử dụng
    fun main() {
        // Đăng ký người dùng mới
        registerUser("john_doe", "password123", "john@example.com")
        registerUser("jane_doe", "securePass", "jane@example.com")

        // Thử đăng nhập với thông tin đúng
        authenticateUser("john_doe", "password123")

        // Thử đăng nhập với thông tin sai
        authenticateUser("john_doe", "wrong_password")
        loginUser("user1")
        cartItems.add(Product(1, "Gundam RX-78-2", "500,000 VND", R.drawable.gundam_sample1, "A legendary Gundam model."))

        println("Giỏ hàng của user1: ${cartItems.map { it.name }}")

        // Người dùng 1 đăng xuất
        logoutUser()

        // Người dùng 2 đăng nhập
        loginUser("user2")
        println("Giỏ hàng của user2: ${cartItems.map { it.name }}")

        // Người dùng 1 đăng nhập lại
        loginUser("user1")
        println("Giỏ hàng của user1: ${cartItems.map { it.name }}")
    }


