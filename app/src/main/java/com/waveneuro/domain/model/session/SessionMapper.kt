package com.waveneuro.domain.model.session

import com.waveneuro.data.api.session.model.session.ApiSessionListRs
import com.waveneuro.data.api.session.model.session.ApiSessionRq

interface SessionMapper {
    fun fromDomainToApi(domain: SessionRq): ApiSessionRq
    fun fromApiToDomain(api: ApiSessionListRs): SessionListRs
}