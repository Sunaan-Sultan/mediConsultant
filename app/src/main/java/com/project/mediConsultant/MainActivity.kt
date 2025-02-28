package com.project.mediConsultant

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.mediConsultant.ui.appbar.AppBar
import com.project.mediConsultant.ui.appbar.BottomNavigationBar
import com.project.mediConsultant.ui.appbar.Navigation
import com.project.mediConsultant.ui.theme.MediConsultantTheme
import com.project.mediConsultant.ui.theme.rememberWindowSizeClass
import kotlinx.coroutines.runBlocking

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mockLoader = MockLoader(this)
        runBlocking {
            mockLoader.init()
        }

        setContent {
            val window = rememberWindowSizeClass()
            MediConsultantTheme(window) {
                val navController = rememberNavController()

                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                LaunchedEffect(currentRoute) {
                    println("Current Route: $currentRoute")
                }

                val title = when (currentRoute) {
                    "productDetail/{productId}" -> "Product Detail"
                    "profile" -> "Profile"
                    "myCart" -> "My Cart"
                    else -> "Medi Consultant"
                }

                val showBackButton = currentRoute == "myCart"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = title,
                            showCartIcon = false,
                            showBackButton = showBackButton,
                            navController = navController
                        )
                    },
                    bottomBar = {
                        if (currentRoute != "productDetail/{productId}" &&
                            currentRoute != "myCart" &&
                            currentRoute != "checkout"
                        ) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Navigation(
                        navController = navController,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}



