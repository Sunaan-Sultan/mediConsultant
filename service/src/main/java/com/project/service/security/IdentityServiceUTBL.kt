package com.project.service.security

import com.project.repository.security.IdentityRepositoryImpl
import com.project.models.security.IdentityService

class IdentityServiceUTBL : IdentityService {
    override fun userExists(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun authenticate(username: String, password: String): Boolean {
        val userRepository = IdentityRepositoryImpl()
        val apiResponse = userRepository.getToken(username, password)
        return apiResponse!!.isNotEmpty()
    }

    override fun biometricRegistered(username: String, password: String): String {
        TODO("Not yet implemented")
    }

    override fun passwordRecovery(
        username: String,
        dateOfBirth: String,
        mobileNumber: String,
        email: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun registered(
        username: String,
        firstname: String,
        lastname: String,
        email: String,
        mobileNumber: String,
        accountCode: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        TODO("Not yet implemented")
    }
}
