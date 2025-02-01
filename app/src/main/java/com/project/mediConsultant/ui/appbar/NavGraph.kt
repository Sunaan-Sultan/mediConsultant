package com.project.mediConsultant.ui.appbar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.mediConsultant.ui.biometricRegistration.BioMetricRegistrationScreen
import com.project.mediConsultant.ui.biometricRegistration.BiometricFingerPrintRegistrationView
import com.project.mediConsultant.ui.biometricRegistration.PreferencesManager
import com.project.mediConsultant.ui.forgetPassword.ForgetPasswordScreen
import com.project.mediConsultant.ui.login.LoginScreen
import com.project.mediConsultant.ui.registration.RegistrationScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, preferencesManager: PreferencesManager) {
    val currentRoute = remember { mutableStateOf(navController.currentBackStackEntry?.destination?.route) }
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, preferencesManager)
        }
        composable("forgetPassword") {
            ForgetPasswordScreen(navController)
        }
        composable("biometricRegistration") {
            BioMetricRegistrationScreen(navController)
        }
        composable("registration") {
            RegistrationScreen(navController)
        }
        composable("fingerprint") {
            BiometricFingerPrintRegistrationView(
                navController = navController,
            )
        }
    }
}
