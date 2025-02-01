package com.project.mediConsultant.ui.biometricRegistration

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.project.mediConsultant.InputFieldValidator
import com.project.mediConsultant.isWithinMaxCharLimit
import com.project.mediConsultant.ui.login.AuthResult
import com.project.mediConsultant.ui.login.LoginPresenter
import com.project.repository.R
import com.project.mediConsultant.ui.snackbar.CustomSnackbarVisuals
import com.project.mediConsultant.ui.theme.BackgroundColor
import com.project.mediConsultant.ui.theme.PrimaryColor
import com.project.mediConsultant.ui.theme.White
import com.project.mediConsultant.ui.theme.getCardColors
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BiometricRegistrationView2(navController: NavHostController) {
    val (backgroundColor, contentColor) = getCardColors()
    var username by remember { mutableStateOf("") }
    var isUsernameError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isValidationErrorDialogVisible by remember { mutableStateOf(false) }
    var enableButtonEnabled by remember { mutableStateOf(false) }
    var enableNextButton by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    var showWebView by remember { mutableStateOf(false) }
    val placeholderTextColor = if (isSystemInDarkTheme()) Color(0x83F1F3F4) else Color.DarkGray
    val context = LocalContext.current
    var successText1 by remember { mutableStateOf("") }
    var shouldNavigateToNextScreen by remember { mutableStateOf(false) }
    val preferencesManager = PreferencesManager(context)
    val getpreferenceData = preferencesManager.getKey("RegistrationKey", "default_value")

    // Validate the fields and get the result and error message
    var (isValidationSuccess, errorMessage, errorFlags) = InputFieldValidator.validateFields(
        username,
        "",
        "",
        "",
        "",
        "",
        password,
        "",
        "",

        isForgetPasswordView = false,
        isRegistrationView = false,
        isBiometricRegistrationView = true,
        isBiometricFingerprintRegistrationView = false,
    )

    val executor = remember { ContextCompat.getMainExecutor(context) }
    // Creating a BiometricPrompt instance for the first step of biometric authentication
    val biometricPromptStepOne = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            // Callback method invoked when an authentication error occurs
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showToast(context, "Authentication error: $errString")
            }

            // Callback method invoked when authentication is successful
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
            }

            // Callback method invoked when authentication fails
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast(context, "Authentication error")
            }
        },
    )

    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    //Snackbar
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData: SnackbarData ->
                    val customVisuals = snackbarData.visuals as? CustomSnackbarVisuals
                    if (customVisuals != null) {
                        Snackbar(
                            snackbarData = snackbarData,
                            contentColor = customVisuals.contentColor,
                            containerColor = customVisuals.containerColor,
                        )
                    } else {
                        Snackbar(snackbarData = snackbarData)
                    }
                },
            )
        },
        backgroundColor = if (isSystemInDarkTheme()) BackgroundColor else Color.White,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center) // Center the Column
                .padding(top = 120.dp, bottom = 0.dp),
        ) {
            Row {
                Column(modifier = Modifier.padding(32.dp)) {
                    TextField(
                        value = username,
                        onValueChange = {
                            if (isWithinMaxCharLimit(it, 40)) {
                                username = it
                            }
                        },
                        placeholder = { Text("Please Enter Username", color = contentColor) },
                        label = { Text("Username", color = contentColor) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Ascii,
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = contentColor,
                            unfocusedLabelColor = contentColor,
                            unfocusedBorderColor = if (isUsernameError) Color.Red else contentColor,
                            focusedBorderColor = if (isUsernameError) Color.Red else contentColor,
                            focusedLabelColor = contentColor,
                            cursorColor = contentColor,
                            leadingIconColor = contentColor,
                            placeholderColor = placeholderTextColor,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp, end = 0.dp, start = 0.dp),
                    )
                    TextField(
                        value = password,
                        onValueChange = {
                            if (isWithinMaxCharLimit(it, 20)) {
                                password = it
                            }
                        },
                        placeholder = { Text("Please Enter Password", color = contentColor) },
                        label = { Text("Password", color = contentColor) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = contentColor,
                            unfocusedLabelColor = contentColor,
                            unfocusedBorderColor = if (isPasswordError) Color.Red else contentColor,
                            focusedBorderColor = if (isPasswordError) Color.Red else contentColor,
                            focusedLabelColor = contentColor,
                            cursorColor = contentColor,
                            leadingIconColor = contentColor,
                            placeholderColor = placeholderTextColor,
                        ),
                        trailingIcon = {
                            val visibilityIcon =
                                if (passwordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
                            val visibilityIconContentDescription =
                                if (passwordVisible) "Hide password" else "Show password"
                            Icon(
                                painter = painterResource(id = visibilityIcon),
                                contentDescription = visibilityIconContentDescription,
                                modifier = Modifier.clickable {
                                    passwordVisible = !passwordVisible
                                },
                                tint = contentColor,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp, end = 0.dp, start = 0.dp),
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(
                            top = 2.5.dp,
                            bottom = 2.5.dp,
                            end = 0.dp,
                            start = 0.dp,
                        ),
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { newValue ->
                                isChecked = newValue
                                enableButtonEnabled = newValue
                            },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = contentColor,
                                checkedColor = contentColor,
                            ),
                            modifier = Modifier.offset(x = (-10).dp),
                        )
                        Text(
                            text = "I Agree to the",
                            color = contentColor,
                            modifier = Modifier.offset(x = (-16).dp),
                            style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center),
                        )
                        ClickableText(
                            text = AnnotatedString("Terms and Conditions"),
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline,
                                color = PrimaryColor,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier.offset(x = (-12).dp),
                            onClick = {
                            },
                        )
                    }

                    Button(
                        onClick = {
                            val loginBean = LoginPresenter()
                            // Perform login authentication
                            val authResult = loginBean.login(context, username, password)

                            // Handle the authentication result
                            when (authResult) {
                                AuthResult.Success -> {
                                    preferencesManager.saveUsername("Username", username)
                                    preferencesManager.saveUsername("Password", password)
                                    navController.navigate("fingerprint")
                                }

                                AuthResult.InvalidUsername -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            CustomSnackbarVisuals(
                                                message = "Wrong username",
                                                contentColor = Color.White,
                                            ),
                                        )
                                    }
                                    keyboardController?.hide()
                                }

                                AuthResult.InvalidPassword -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            CustomSnackbarVisuals(
                                                message = "Wrong password",
                                                contentColor = Color.White,
                                            ),
                                        )
                                    }
                                    keyboardController?.hide()
                                }

                                else -> {}
                            }
                        },
                        modifier = Modifier
                            .padding(start = 0.dp, top = 16.dp, end = 0.dp, bottom = 0.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = PrimaryColor,
                            contentColor = White,
                        ),
                        shape = RoundedCornerShape(20.dp),
                        enabled = enableButtonEnabled,

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "Next",
                                color = White,
                                modifier = Modifier.padding(5.dp),
                                fontSize = 16.sp,
                            )

//                        Icon(
//                            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
//                            contentDescription = null,
//                            tint = contentColor,
//                        )
                        }
                        if (isValidationErrorDialogVisible) {
                            AlertDialog(
                                onDismissRequest = {
                                },
                                title = {
                                    Image(
                                        painter = if (errorMessage == "Success") {
                                            painterResource(id = R.drawable.success)
                                        } else {
                                            painterResource(id = R.drawable.high_importance_72)
                                        },
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(45.dp),
                                    )
                                },
                                text = {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            if (errorMessage == "Success") {
                                                "Success."
                                            } else {
                                                errorMessage
                                            },
                                            color = contentColor,
                                            modifier = Modifier.align(Alignment.Center),
                                            style = MaterialTheme.typography.body1
                                                .copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp,
                                                    color = contentColor,
                                                    textAlign = TextAlign.Center,
                                                ),
                                        )
                                    }
                                },
                                confirmButton = {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Button(
                                            onClick = {
                                                if (errorMessage == "Success") {
                                                } else {
                                                    isValidationErrorDialogVisible = false
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = PrimaryColor,
                                                contentColor = White,
                                            ),
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(bottom = 12.dp),
                                        ) {
                                            Text("OK")
                                        }
                                    }
                                },
                                backgroundColor = backgroundColor,
                            )
                        }
                    }
                }
            }
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
