package com.project.mediConsultant.ui.biometricRegistration


import android.content.Context
import com.project.service.security.SecurityFactory

class BiometricRegistrationPresenter(context: Context) {
    private val preferencesManager = PreferencesManager(context)

    fun registration(username: String, password: String): String {
        val identityService = SecurityFactory.getIdentityService("LEADS")
        val identityResponse = identityService.biometricRegistered(username, password)
        preferencesManager.saveKey("RegistrationKey", identityResponse)
        return identityResponse
    }
}
