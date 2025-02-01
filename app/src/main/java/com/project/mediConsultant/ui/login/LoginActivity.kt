package com.project.mediConsultant.ui.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.mediConsultant.ui.biometricRegistration.PreferencesManager
import com.project.mediConsultant.ui.appbar.NavGraph
import com.project.mediConsultant.ui.appbar.SetStatusBarColor
import com.project.mediConsultant.ui.theme.AppTheme
import com.project.mediConsultant.ui.theme.BackgroundColor
import com.project.mediConsultant.ui.theme.BottomBarColor2
import com.project.mediConsultant.ui.theme.MediConsultantTheme
import com.project.mediConsultant.ui.theme.Orientation
import com.project.mediConsultant.ui.theme.White
import com.project.mediConsultant.ui.theme.rememberWindowSizeClass

class LogInActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navigationBarColor = if (isSystemInDarkTheme()) BottomBarColor2 else White
            val navController = rememberNavController()
            val preferencesManager = PreferencesManager(applicationContext)
            SetStatusBarColor(Color(0xFF2E75A6), navigationBarColor)
            val window = rememberWindowSizeClass()
            MediConsultantTheme(window) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (isSystemInDarkTheme()) BackgroundColor else Color.White),
                ) {
                    NavGraph(navController = navController, preferencesManager)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavHostController, preferencesManager: PreferencesManager) {
    val window = rememberWindowSizeClass()
    MediConsultantTheme(window) {
        if (AppTheme.orientation == Orientation.Portrait) {
            LoginView(navController, preferencesManager)
        }
    }
}