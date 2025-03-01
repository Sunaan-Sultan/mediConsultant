package com.project.repository.security

import com.project.repository.RestUtil.BASE_URL
import com.project.models.security.Identity
import com.project.models.security.IdentityRepository
import com.project.models.users.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

var authToken: String? = null
class IdentityRepositoryImpl : IdentityRepository {
    private val TOKEN_PATH: String = "/tokens"
    override fun userExists(username: String): Boolean {
        TODO("Not yet implemented")
    }

    @OptIn(InternalAPI::class)
    override fun getToken(username: String, password: String): String {
        var response: String? = null
        runBlocking {
            try {
                val client = HttpClient() {
                    install(ContentNegotiation) {
                        json()
                    }
                }

                val identity = Identity(username, password)
                val res: String = client.post("$BASE_URL$TOKEN_PATH") {
                    contentType(ContentType.Application.Json)
                    body = identity
                }.body()

                val jsonObject = Json.parseToJsonElement(res ?: "").jsonObject
                val token = jsonObject["token"]?.jsonPrimitive?.contentOrNull
                authToken = token
                response = token
            } catch (e: Exception) {
            }
        }
        return response!!
    }

    override fun getUser(username: String): User? {
        TODO("Not yet implemented")
    }

    override fun getBiometricRegistrationToken(username: String, password: String): String {
        TODO("Not yet implemented")
    }

    override fun getPasswordRecoveryToken(
        username: String,
        dateOfBirth: String,
        mobileNumber: String,
        email: String,
    ): Boolean {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }
}
