package com.project.repository.security

import com.project.models.security.Identity
import com.project.models.security.IdentityRepository

class IdentityLocalRepositoryImpl : IdentityRepository {

    private val identityList = listOf(
        Identity("medico", "Medi@123"),
    )

    override fun userExists(username: String): Boolean {
        return identityList.any { it.username == username }
    }

    override fun getToken(username: String, password: String): String {
        return if (username == "medico" && password == "Medi@123") {
            "hhfghfdg46456456"
        } else {
            ""
        }
    }

    override fun getBiometricRegistrationToken(username: String, password: String): String {
        return if (username == "medico" && password == "Medi@123") {
            "okay"
        } else {
            ""
        }
    }
    override fun getPasswordRecoveryToken(
        username: String,
        dateOfBirth: String,
        mobileNumber: String,
        email: String,
    ): Boolean {
        return true
    }

    override fun getRegistrationToken(
        username: String,
        firstname: String,
        lastname: String,
        email: String,
        mobileNumber: String,
        accountCode: String,
        password: String,
        confirmPassword: String,
    ): String {
        return "hello"
    }
}
