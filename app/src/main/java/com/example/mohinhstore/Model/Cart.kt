package com.example.mohinhstore.Model

class Cart(
    val userId: Int,       // ID người dùng
    val items: List<CartItem>, // Danh sách các sản phẩm trong giỏ
    val totalPrice: String // Tổng giá trị giỏ hàng (VD: "1,500,000 VND")
)