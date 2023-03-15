package com.waveneuro.domain.model.client

data class ClientInfo(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
    val organizations: List<Organization>
)
