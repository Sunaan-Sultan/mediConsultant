package com.project.service.security

import com.project.models.security.IdentityService

class IdentityServiceImpl : IdentityService {
    override fun authenticate(username: String, password: String): Boolean {
        val identityRepository = SecurityFactory.getIdentityRepository()
        val identityResponse = identityRepository.getToken(username, password)
        return identityResponse.isNotEmpty()
    }

    override fun biometricRegistered(username: String, password: String): String {
        val identityRepository = SecurityFactory.getIdentityRepository()
        val identityResponse = identityRepository.getBiometricRegistrationToken(username, password)
        return identityResponse
    }
    override fun passwordRecovery(
        username: String,
        dateOfBirth: String,
        mobileNumber: String,
        email: String,
    ): Boolean {
        val identityRepository = SecurityFactory.getIdentityRepository()
        val identityResponse = identityRepository.getPasswordRecoveryToken(username, dateOfBirth, mobileNumber, email)
        return identityResponse
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
        val identityRepository = SecurityFactory.getIdentityRepository()
        val identityResponse = identityRepository.getRegistrationToken(
            username,
            firstname,
            lastname,
            email,
            mobileNumber,
            accountCode,
            password,
            confirmPassword
        )
        return identityResponse.isNotEmpty()
    }

    override fun userExists(username: String): Boolean {
        val identityRepository = SecurityFactory.getIdentityRepository()
        return identityRepository.userExists(username)
    }
}
