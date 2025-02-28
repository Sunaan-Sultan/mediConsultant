package com.project.mediConsultant.ui.registration

import com.project.service.security.SecurityFactory

class RegistrationPresenter {
    fun registration(username: String, firstname: String, lastname: String, email: String, mobileNumber: String, accountCode: String, password: String, confirmPassword: String): Boolean {
        val identityService = SecurityFactory.getIdentityService("MEDICONSULTANT")

        return identityService.registered(username, firstname, lastname, email, mobileNumber, accountCode, password, confirmPassword)
    }
}
