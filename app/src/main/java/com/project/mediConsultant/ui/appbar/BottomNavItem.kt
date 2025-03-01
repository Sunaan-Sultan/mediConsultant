package com.project.mediConsultant.ui.appbar

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.models.product.Product
import com.project.mediConsultant.ui.cart.CheckoutScreen
import com.project.mediConsultant.ui.cart.MyCartScreen
import com.project.mediConsultant.ui.home.HomeScreen
import com.project.mediConsultant.ui.home.admin.AdminHomeScreen
import com.project.mediConsultant.ui.home.member.MemberHomeScreen
import com.project.mediConsultant.ui.home.provider.ProviderHomeScreen
import com.project.mediConsultant.ui.product.ProductDetailScreen
import com.project.mediConsultant.ui.product.ProductScreen
import com.project.mediConsultant.ui.product.ProductView
import com.project.mediConsultant.ui.profile.ProfileScreen
import com.project.service.product.ProductServiceImpl

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Product : BottomNavItem("products", "Product", Icons.Filled.ShoppingCart)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
    object MyCart : BottomNavItem("myCart", "MyCart", Icons.Filled.Person)
}

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: String,
    userRole: String
) {
    val cartItems = remember { mutableStateListOf<Product>() }
    val context = LocalContext.current
    val productService = ProductServiceImpl()
    val products = remember { productService.getProductService(context) } // Fetch products

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("home") {
            when (userRole.lowercase()) {
                "admin" -> AdminHomeScreen()
                "provider" -> ProviderHomeScreen()
                "member" -> MemberHomeScreen()
                else -> HomeScreen(
                    context = LocalContext.current,
                    onCategoryClick = { category ->
                        navController.navigate("products/$category")
                    },
                    onProductClick = { product ->
                        navController.navigate("productDetail/${product.id}")
                    }
                )
            }
        }

        composable("products/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductScreen(navController, category)
        }

        composable("products") {
            ProductView(
                context = LocalContext.current,
                onCategoryClick = { category ->
                    navController.navigate("products/$category")
                },
                onProductClick = { product ->
                    navController.navigate("productDetail/${product.id}")
                }
            )
        }

        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: -1
            val context = LocalContext.current
            val productService = ProductServiceImpl()
            val product = productService.getProductService(context).find { it.id == productId }
            product?.let {
                ProductDetailScreen(product = it, navController = navController)
            }
        }

        composable("profile") {
            ProfileScreen()
        }

        composable("myCart") {
            MyCartScreen(
                products = products,
                onCheckoutClick = { selectedProducts, totalPrice ->
                    // Navigate to the CheckoutScreen
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "selectedProducts",
                        selectedProducts
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set("totalPrice", totalPrice)
                    navController.navigate("checkout")
                }
            )
        }

        composable("checkout") {
            val selectedProducts = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<Pair<Product, Int>>>("selectedProducts") ?: emptyList()

            val totalPrice = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Double>("totalPrice") ?: 0.0

            CheckoutScreen(
                selectedProducts = selectedProducts,
                totalPrice = totalPrice,
                onMakePayment = {
                    Log.d("CheckoutScreen", "Payment made for $${"%.2f".format(totalPrice)}")
                    // Navigate back to home after payment
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        composable("admin_home") {
            AdminHomeScreen()
        }
        composable("provider_home") {
            ProviderHomeScreen()
        }
        composable("member_home") {
            MemberHomeScreen()
        }


    }
}

