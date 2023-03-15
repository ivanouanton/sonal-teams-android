package com.waveneuro.domain.model.session

import com.waveneuro.data.api.user.model.session.ApiSessionListRs
import com.waveneuro.data.api.user.model.session.ApiSessionRq

interface SessionMapper {
    fun fromDomainToApi(domain: SessionRq): ApiSessionRq
    fun fromApiToDomain(api: ApiSessionListRs): SessionListRs
}