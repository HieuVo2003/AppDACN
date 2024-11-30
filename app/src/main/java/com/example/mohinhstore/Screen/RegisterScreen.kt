package com.example.mohinhstore.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mohinhstore.Model.User
import com.example.mohinhstore.userList

@Composable
fun RegisterScreen(
    navController: NavHostController,
    onRegisterSuccess: (User) -> Unit
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
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
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Xử lý đăng ký
                val user = registerUser(username.value, password.value, email.value)
                if (user != null) {
                    onRegisterSuccess(user)
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                } else {
                    errorMessage.value = "Tên đăng nhập đã tồn tại"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng ký")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage.value,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

val userList = mutableListOf<User>()
// Hàm đăng ký người dùng
fun registerUser(username: String, password: String, email: String): User? {
    // Kiểm tra nếu tài khoản hoặc email đã tồn tại
    val existingUser = userList.find { it.username == username || it.email == email }
    if (existingUser != null) {
        println("Tên tài khoản hoặc email đã tồn tại!")
        return null // Trả về null nếu đăng ký thất bại
    }

    // Tạo người dùng mới và thêm vào danh sách
    val newUser = User(username = username, password = password, email = email)
    userList.add(newUser)

    println("Đăng ký thành công!")
    return newUser
}