package com.waveneuro.domain.model.user

data class UserInfo(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
    val organizations: List<Organization>
)
