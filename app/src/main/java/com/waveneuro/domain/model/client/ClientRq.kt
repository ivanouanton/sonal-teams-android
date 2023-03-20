package com.waveneuro.domain.model.client

import com.waveneuro.domain.model.common.SexType

data class ClientRq(
    val firstName: String,
    val lastName: String,
    val birthday: String?,
    val sex: SexType,
)
