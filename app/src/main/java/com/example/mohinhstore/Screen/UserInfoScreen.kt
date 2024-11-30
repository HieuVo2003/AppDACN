package com.example.mohinhstore.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mohinhstore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    navController: NavHostController,
    onLogout: () -> Unit // Callback xử lý logic đăng xuất
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông Tin Người Dùng") },
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
                painter = painterResource(id = R.drawable.ic_user_avatar),
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
                onClick = { /* Thực hiện xử lý chỉnh sửa thông tin */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Chỉnh sửa thông tin")
            }

            // Nút đăng xuất
            Button(
                onClick = { onLogout() }, // Gọi callback xử lý đăng xuất
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Đăng xuất")
            }
        }
    }
}
