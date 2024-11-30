package com.example.mohinhstore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mohinhstore.getProductById

@Composable
fun ProductDetailScreen(productId: Int) {
    val product = getProductById(productId)
    if (product != null) {
        Column(modifier = Modifier.padding(16.dp)) {
//                Image(
//                    painter = painterResource(id = product.imageResId),
//                    contentDescription = product.name,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp)
//                )
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
//                Text(
//                    text = product.description,
//                    fontSize = 16.sp,
//                    modifier = Modifier.padding(top = 16.dp)
//                )
        }
    } else {
        Text(text = "Product not found!")
    }
}
