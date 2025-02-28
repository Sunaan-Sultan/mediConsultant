package com.project.mediConsultant.ui.biometricRegistration

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.mediConsultant.ui.theme.BackgroundColor
import com.project.mediConsultant.ui.appbar.AppBar
import com.project.mediConsultant.ui.theme.MediConsultantTheme
import com.project.mediConsultant.ui.theme.rememberWindowSizeClass

@Composable
fun BioMetricRegistrationScreen(navController: NavHostController) {
    val profilePhoto: Painter = painterResource(id = com.project.repository.R.drawable.profile)
    val onProfileClick: () -> Unit = {
    }
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    var title by remember { mutableStateOf("") }
    val getpreferenceData = preferencesManager.getKey("RegistrationKey", "default_value")
    if (getpreferenceData == "okay") {
        title = "Disable"
    } else {
        title = "Enable"
    }
    val currentRoute = remember { mutableStateOf(navController.currentBackStackEntry?.destination?.route) }
    Column {
        AppBar(
            navController = navController,
            showCartIcon = false,
            showBackButton = true,
            title = "$title TouchID",
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) BackgroundColor else Color.White)
                .padding(bottom = 20.dp),
        ) {
            val window = rememberWindowSizeClass()
            MediConsultantTheme(window) {
                BiometricRegistrationView2(navController)
            }
        }
    }
}
