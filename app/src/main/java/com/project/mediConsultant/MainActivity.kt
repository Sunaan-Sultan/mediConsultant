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

                // Debugging to confirm the current route
                LaunchedEffect(currentRoute) {
                    println("Current Route: $currentRoute")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title =
                            if (currentRoute == "productDetail/{productId}") "Product Detail"
                            else if (currentRoute == "profile") "Profile"
                            else if (currentRoute == "myCart") "My Cart"
                            else "Medi Consultant",
                            //showCartIcon = currentRoute != "productDetail/{productId}",
                            showCartIcon = false,
                            navController
                        )
                    },
                    bottomBar = {
                        if (currentRoute != "productDetail/{productId}" && currentRoute != "myCart" && currentRoute != "checkout") {
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



