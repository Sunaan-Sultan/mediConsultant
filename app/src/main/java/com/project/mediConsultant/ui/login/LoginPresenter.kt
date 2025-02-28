package com.project.mediConsultant.ui.login

import android.content.Context
import com.project.service.security.SecurityFactory

// Sealed class to represent different authentication results
sealed class AuthResult {
    object Success : AuthResult()
    object InvalidUsername : AuthResult()
    object InvalidPassword : AuthResult()
}

// Presenter class for handling login functionality
class LoginPresenter {
    /**
     * Performs login authentication.
     *
     * @param context The context.
     * @param username The provided username.
     * @param password The provided password.
     * @return An instance of AuthResult representing the authentication result.
     */
    fun login(
        context: Context,
        username: String,
        password: String,
    ): AuthResult {
        val identityService = SecurityFactory.getIdentityService("MEDICONSULTANT")

        // Perform authentication using the identity service
        return if (identityService.authenticate(username, password)) {
            AuthResult.Success
        } else {
            if (!identityService.userExists(username)) {
                AuthResult.InvalidUsername
            } else {
                AuthResult.InvalidPassword
            }
        }
    }
}