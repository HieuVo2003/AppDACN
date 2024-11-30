package com.example.mohinhstore.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AuthViewModel : ViewModel() {
    // Trạng thái đăng nhập
    var isLoggedIn by mutableStateOf(false)
        private set

    // Hàm đăng nhập (giả lập)
    fun login() {
        isLoggedIn = true
    }

    // Hàm đăng xuất
    fun logout() {
        isLoggedIn = false
    }
}