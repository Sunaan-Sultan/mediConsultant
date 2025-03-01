package com.project.models.users

data class User(
    val id: Int,
    val username: String,
    val password: String, // ideally store a hashed password in production
    val isMember: Boolean,
    val userRole: String // "admin", "provider", or "member"
)

