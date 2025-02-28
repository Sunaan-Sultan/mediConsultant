package com.project.mediConsultant.ui.biometricRegistration

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.project.mediConsultant.ui.theme.PrimaryColor
import com.project.mediConsultant.ui.theme.White
import com.project.mediConsultant.ui.theme.getCardColors
import kotlinx.coroutines.runBlocking
import com.project.repository.R

object GlobalVariables {
    var bioFirstCard: Int = 0
}

@Composable
fun BioMetricRegistrationView(navController: NavHostController) {
    val (backgroundColor, textColor) = getCardColors()
    var currentStep by rememberSaveable { mutableStateOf(1) }

    var biosecondCard by remember {
        mutableStateOf(false)
    }
    var firstCard by remember {
        mutableStateOf(false)
    }
    var firstCardEnabel by remember {
        mutableStateOf(true)
    }
    var secondCard by remember {
        mutableStateOf(false)
    }
    var secondCardEnabel by remember {
        mutableStateOf(false)
    }
    var thirdCard by remember {
        mutableStateOf(false)
    }
    var thirdCardEnable by remember {
        mutableStateOf(false)
    }
    var fourthCard by remember {
        mutableStateOf(false)
    }
    var fourthCardEnable by remember {
        mutableStateOf(false)
    }
    var username by remember { mutableStateOf("") }
    var isUsernameError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isValidationErrorDialogVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var enableButtonEnabled by remember { mutableStateOf(false) }

    var isChecked by remember {
        mutableStateOf(false)
    }
    var showWebView by remember {
        mutableStateOf(false)
    }
    val placeholderTextColor = if (isSystemInDarkTheme()) Color(0x83F1F3F4) else Color.DarkGray
    val context = LocalContext.current
    var successText1 by remember {
        mutableStateOf("")
    }

    val preferencesManager = PreferencesManager(context)
    val getpreferenceData = preferencesManager.getKey("RegistrationKey", "default_value")

    val executor = remember { ContextCompat.getMainExecutor(context) }

    val biometricPromptStepOne = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showToast(context, "Authentication error: $errString", 0)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showToast(context, "Successfully Authenticate", 2)
                secondCardEnabel = true
                firstCard = false
                firstCardEnabel = false
                if (currentStep <= 4) currentStep++
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast(context, "Authentication error", 0)
            }
        },
    )

    val biometricPromptStepTwo = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showToast(context, "Authentication error: $errString", 0)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showToast(context, "Successfully Authenticate", 2)
                if (currentStep == 1) {
                    secondCardEnabel = true
                    firstCard = false
                    firstCardEnabel = false
                    if (currentStep <= 4) {
                        currentStep++
                    }
                    biometricPromptStepOne.cancelAuthentication()
                } else {
                    thirdCardEnable = true
                    secondCardEnabel = false
                    secondCard = false
                    if (currentStep <= 4) currentStep++
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast(context, "Failed to Authentication", 0)
            }
        },
    )

    Column {
        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
        ) {
            Row {
                Text(
                    text = "Enable Fingerprint Login",
                    style = MaterialTheme.typography.body1,
                    color = textColor,
                )
            }
            Row {
                Stepper(
                    modifier = Modifier.fillMaxWidth(),
                    numberOfSteps = 4,
                    currentStep = currentStep,
                    selectedColor = PrimaryColor,
                    unSelectedColor = Color(0xffd9d9d9),
                )
            }
        }

        Column(modifier = Modifier.padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
            Row(Modifier.padding(bottom = 10.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (firstCardEnabel) {
                                showWebView = true
                                val promptInfo = BiometricPrompt.PromptInfo
                                    .Builder()
                                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                                    .setTitle("Verify that it's you")
                                    .setSubtitle("Use your fingerprint to continue")
                                    .setNegativeButtonText("Cancel")
                                    .build()
                                biometricPromptStepOne.authenticate(promptInfo)

                                if (GlobalVariables.bioFirstCard == 2) {
                                    Log.d("heelloo", "heelloo")
                                }
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    backgroundColor = if (firstCardEnabel) backgroundColor else Color.LightGray,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                                .drawWithContent {
                                    drawContent()
                                    drawLine(
                                        color = textColor,
                                        start = Offset(size.width, 0f),
                                        end = Offset(size.width, this.size.height),
                                        strokeWidth = 2f,
                                    )
                                },
                        ) {
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.body1,
                                color = textColor,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(4f)
                                .padding(start = 4.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
                        ) {
                            Text(
                                text = " First Fingerprint Validation",
                                style = MaterialTheme.typography.body1,
                                color = textColor,
                            )
                        }
                    }
                }
            }
            if (firstCardEnabel) {
                Row(modifier = Modifier.clickable() {  }) {
                    Text(
                        text = " Please Enter your Finger Print for the first time",
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                        color = textColor,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
            Row(Modifier.padding(bottom = 10.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (secondCardEnabel) {
                                secondCard = true
                                runBlocking {
                                    val promptInfo = BiometricPrompt.PromptInfo
                                        .Builder()
                                        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                                        .setTitle("Verify that it's you")
                                        .setSubtitle("Use your fingerprint to continue")
                                        .setNegativeButtonText("Cancel")
                                        .build()

                                    biometricPromptStepTwo.authenticate(promptInfo)
                                }
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    backgroundColor = if (secondCardEnabel) backgroundColor else Color.LightGray,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                                .drawWithContent {
                                    drawContent()
                                    drawLine(
                                        color = textColor,
                                        start = Offset(size.width, 0f),
                                        end = Offset(size.width, this.size.height),
                                        strokeWidth = 2f,
                                    )
                                },
                        ) {
                            Text(
                                text = "2",
                                style = MaterialTheme.typography.body1,
                                color = textColor,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(4f)
                                .padding(start = 4.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
                        ) {
                            Text(
                                text = " Second Fingerprint Validation",
                                style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                color = textColor,
                            )
                        }
                    }
                }
            }
            if (secondCardEnabel) {
                Row(
                    modifier = Modifier.clickable() {
                        //secondCard = false
                    },
                ) {
                    Text(
                        text = " Please Enter your Finger Print for the second time",
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                        color = textColor,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),

                    )
                }
            }
        }
        Column(modifier = Modifier.padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
            Row(Modifier.padding(bottom = 10.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (thirdCardEnable) {
                                firstCard = false
                                secondCard = false
                                thirdCard = true
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    backgroundColor = if (thirdCardEnable) backgroundColor else Color.LightGray,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                                .drawWithContent {
                                    drawContent()
                                    drawLine(
                                        color = textColor,
                                        start = Offset(size.width, 0f),
                                        end = Offset(size.width, this.size.height),
                                        strokeWidth = 2f,
                                    )
                                },
                        ) {
                            Text(
                                text = "3",
                                style = MaterialTheme.typography.body1,
                                color = textColor,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(4f)
                                .padding(start = 4.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
                        ) {
                            Text(
                                text = "Customer credential checking",
                                style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                color = textColor,
                            )
                        }
                    }
                }
            }

            if (thirdCardEnable) {
                fun validateFields(username: String, password: String): Boolean {
                    isUsernameError = username.isEmpty()

                    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$")
                    isPasswordError = !passwordRegex.matches(password)

                    if (isUsernameError || isPasswordError) {
                        isValidationErrorDialogVisible = true
                    }

                    return !isUsernameError && !isPasswordError
                }

                fun getErrorMessage(): String {
                    return when {
                        isUsernameError -> "Enter valid username"
                        isPasswordError -> "Enter valid password. Minimum eight characters, at least one uppercase letter, one lowercase letter and one number"

                        else -> "Success"
                    }
                }

                Row(
                    modifier = Modifier
                        .clickable {
                            thirdCard = false
                        }
                        .padding(
                            top = 0.dp,
                            bottom = 0.dp,
                            start = 48.dp,
                            end = 48.dp,
                        ),
                ) {
                    Column {
                        TextField(
                            value = username,
                            onValueChange = {
                                username = it
                            },
                            placeholder = { if (!isUsernameError) Text("Username") else Text("Please Enter Username") },
                            label = { Text("Username") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Ascii,
                ),
                singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = textColor,
                                unfocusedLabelColor = textColor,
                                unfocusedBorderColor = if (isUsernameError) Color.Red else textColor,
                                focusedBorderColor = if (isUsernameError) Color.Red else textColor,
                                focusedLabelColor = textColor,
                                cursorColor = textColor,
                                leadingIconColor = textColor,
                                placeholderColor = placeholderTextColor,

                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        TextField(
                            value = password,
                            onValueChange = {
                                password = it
                            },
                            placeholder = { if (!isPasswordError) Text("Password") else Text("Please Enter Password") },
                            label = { Text("Password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = textColor,
                                unfocusedLabelColor = textColor,
                                unfocusedBorderColor = if (isPasswordError) Color.Red else textColor,
                                focusedBorderColor = if (isPasswordError) Color.Red else textColor,
                                focusedLabelColor = textColor,
                                cursorColor = textColor,
                                leadingIconColor = textColor,
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
                                    tint = textColor,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { newValue ->
                                    isChecked = newValue
                                    enableButtonEnabled = newValue
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = textColor,
                                    checkedColor = textColor,
                                ),
                                modifier = Modifier.offset(x = (-10).dp),
                            )
                            Text(
                                text = "I Agree the",
                                color = textColor,
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
                        if (getpreferenceData == "okay") {
                            Button(
                                onClick = {
                                    if (validateFields(username, password)) {
                                        preferencesManager.removeKey("RegistrationKey")
                                        fourthCardEnable = true
                                        thirdCard = false
                                        thirdCardEnable = false
                                        if (currentStep <= 4) currentStep++
                                    } else {
                                        errorMessage = "Invalid input. Please check your username and password."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),

                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = PrimaryColor,
                                    contentColor = White,
                                ),
                                shape = RoundedCornerShape(20.dp),
                                enabled = enableButtonEnabled,
                            ) {
                                Text(
                                    text = "Disable fingerprint login",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(5.dp),
                                    color = Color.White,
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (validateFields(username, password)) {
                                        val registrationBean =
                                            BiometricRegistrationPresenter(context)
                                        val token =
                                            registrationBean.registration(username, password)
                                        if (token.isNotEmpty()) {
                                            fourthCardEnable = true
                                            thirdCard = false
                                            thirdCardEnable = false
                                            if (currentStep <= 4) currentStep++
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Try it again",
                                                Toast.LENGTH_SHORT,
                                            )
                                                .show()
                                        }
                                    } else {
                                        errorMessage = "Invalid input. Please check your username and password."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),

                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = PrimaryColor,
                                    contentColor = White,
                                ),
                                shape = RoundedCornerShape(20.dp),
                                enabled = enableButtonEnabled,
                            ) {
                                Text(
                                    text = "Enable fingerprint login",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(5.dp),
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
                if (isValidationErrorDialogVisible) {
                    AlertDialog(
                        onDismissRequest = {
                            isValidationErrorDialogVisible = false
                        },
                        title = {
                            Image(
                                painter = if (getErrorMessage() == "Success") {
                                    painterResource(id = R.drawable.success)
                                } else {
                                    painterResource(id = R.drawable.close)
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
                                    if (getErrorMessage() == "Success") {
                                        "Success."
                                    } else {
                                        getErrorMessage()
                                    },
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.body1
                                        .copy(
                                            fontWeight = FontWeight.Bold,
                                            color = textColor,
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
                                        if (getErrorMessage() == "Success") {
                                            isValidationErrorDialogVisible = false
                                            navController.navigate("login")
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
                                    Text("Done")
                                }
                            }
                        },
                        backgroundColor = backgroundColor,
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp)) {
            Row(Modifier.padding(bottom = 10.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (fourthCardEnable) {
                                firstCard = false
                                secondCard = false
                                thirdCard = false
                                fourthCard = true
                                if (currentStep <= 4) currentStep++
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    backgroundColor = if (fourthCardEnable) backgroundColor else Color.LightGray,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                                .drawWithContent {
                                    drawContent()
                                    drawLine(
                                        color = textColor,
                                        start = Offset(size.width, 0f),
                                        end = Offset(size.width, this.size.height),
                                        strokeWidth = 2f,
                                    )
                                },
                        ) {
                            Text(
                                text = "4",
                                style = MaterialTheme.typography.body1,
                                color = textColor,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(4f)
                                .padding(start = 4.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
                        ) {
                            Text(
                                text = "Confirmation",
                                style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                color = textColor,
                            )
                        }
                    }
                }
            }
            if (fourthCardEnable) {
                Row(
                    modifier = Modifier.clickable {
                    },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = " Your Password Id is enable.You can login now with your smartphone fingerprint",
                            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                            color = textColor,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        )

                        Button(
                            onClick = {
                                fourthCard = true
                                navController.navigate("login")
                            },
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.CenterHorizontally),

                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryColor,
                                contentColor = White,
                            ),
                            shape = RoundedCornerShape(20.dp),

                        ) {
                            Text(
                                text = "Okay",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(5.dp),
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun showToast(context: Context, message: String, isSuccess: Int) {
    GlobalVariables.bioFirstCard = isSuccess
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
