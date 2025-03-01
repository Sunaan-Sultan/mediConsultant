package com.project.mediConsultant.ui.login

import android.content.Context
import com.project.models.users.User
import com.project.service.security.SecurityFactory

// Sealed class to represent different authentication results
sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    object InvalidUsername : AuthResult()
    object InvalidPassword : AuthResult()
}

// Presenter class for handling login functionality
class LoginPresenter(private val context: Context) {
    /**
     * Performs login authentication.
     *
     * @param context The context.
     * @param username The provided username.
     * @param password The provided password.
     * @return An instance of AuthResult representing the authentication result.
     */
    fun login(username: String, password: String): AuthResult {
        val identityService = SecurityFactory.getIdentityService(context,"MEDICONSULTANT")
        return if (identityService.authenticate(username, password)) {
            // Retrieve user details after successful authentication
            val user = identityService.getUser(username)
            if (user != null) {
                AuthResult.Success(user)
            } else {
                // This case should rarely happen if authentication passed
                AuthResult.InvalidUsername
            }
        } else {
            if (!identityService.userExists(username)) {
                AuthResult.InvalidUsername
            } else {
                AuthResult.InvalidPassword
            }
        }
    }
}