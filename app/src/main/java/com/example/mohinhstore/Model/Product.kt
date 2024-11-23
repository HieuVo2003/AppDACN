package com.example.mohinhstore.Model

class Product(
    val id: Int,           // ID duy nhất cho sản phẩm
    val name: String,      // Tên sản phẩm
    val price: String,     // Giá sản phẩm (VD: "500,000 VND")
    val imageResId: Int,   // ID tài nguyên hình ảnh (hoặc URL)
    val categoryId: Int    // ID của danh mục sản phẩm
)