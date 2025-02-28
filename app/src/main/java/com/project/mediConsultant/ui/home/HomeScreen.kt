package com.project.mediConsultant.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.models.product.Product
import com.project.mediConsultant.ui.category.CategoryGrid
import com.project.mediConsultant.ui.product.ProductList
import com.project.service.product.ProductServiceImpl
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.project.mediConsultant.ui.cart.CartManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    context: Context,
    onCategoryClick: (String) -> Unit,
    onProductClick: (Product) -> Unit
) {
    val productService = ProductServiceImpl()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Load products asynchronously
    LaunchedEffect(Unit) {
        val loadedProducts = withContext(Dispatchers.IO) {
            productService.getProductService(context)
        }
        products = loadedProducts
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp )
    ) {

    }
}





