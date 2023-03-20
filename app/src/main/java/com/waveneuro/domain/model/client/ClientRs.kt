package com.waveneuro.domain.model.client

import com.waveneuro.domain.model.common.TosStatus
import com.waveneuro.domain.model.user.Organization

data class ClientRs(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthday: String?,
    val email: String,
    val username: String?,
    val isMale: Boolean,
    val organization: Organization,
    val isTosSigned: Boolean,
    val tosStatus: TosStatus,
    val alternativeIds: List<AlternativeId>?,
    val imageURLString: String?,
)
