package com.waveneuro.domain.model.client

data class ClientListRs(
    val hasNext: Boolean,
    val hasPrev: Boolean,
    val nextNum: Int?,
    val page: Int,
    val pages: Int,
    val patients: List<ClientRs>,
    val total: Int,
)