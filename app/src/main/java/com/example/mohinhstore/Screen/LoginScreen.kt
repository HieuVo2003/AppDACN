package com.example.mohinhstore.Screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mohinhstore.Model.User

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: (User) -> Unit
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Tên đăng nhập") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                // Xác thực tài khoản
                val user = authenticateUser(username.value, password.value)
                if (user != null) {
                    onLoginSuccess(user) // Lưu thông tin người dùng
                    navController.navigate(route = "home") {
                        popUpTo(route = "login") { inclusive = true } // Xóa login khỏi stack
                    }
                } else {
                    errorMessage.value = "Sai tên đăng nhập hoặc mật khẩu"
                }
            },
            modifier = Modifier.height(48.dp) // Cấu hình giao diện nút
        ) {
            Text("Đăng nhập")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage.value,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chưa có tài khoản? Đăng ký",
            modifier = Modifier.clickable { navController.navigate("register") },
            color = MaterialTheme.colorScheme.primary
        )
    }
}


fun authenticateUser(username: String, password: String): User? {
    // Tìm người dùng với tên tài khoản và mật khẩu khớp
    val user = com.example.mohinhstore.userList.find { it.username == username && it.password == password }
    return if (user != null) {
        println("Đăng nhập thành công!")
        user // Trả về thông tin người dùng nếu xác thực thành công
    } else {
        println("Sai tên đăng nhập hoặc mật khẩu!")
        null // Trả về null nếu xác thực thất bại
    }
}
@Composable
fun AnimatedLoginScreen(isVisible: Boolean, onLoginSuccess: (User) -> Unit) {
    AnimatedVisibility(visible = isVisible) {
        LoginScreen(navController = rememberNavController(), onLoginSuccess = onLoginSuccess)
    }
}
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginWithTransition(loggedIn: Boolean, onLoginSuccess: (User) -> Unit) {
    Crossfade(targetState = loggedIn) { isLoggedIn ->
        if (!isLoggedIn) {
            LoginScreen(navController = rememberNavController(), onLoginSuccess = onLoginSuccess)
        } else {
            HomeScreen(navController = rememberNavController(), cartItems = mutableStateListOf())
        }
    }
}

