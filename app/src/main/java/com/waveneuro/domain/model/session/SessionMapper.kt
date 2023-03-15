package com.waveneuro.domain.model.session

import com.waveneuro.data.api.user.model.session.ApiSessionRq

interface SessionMapper {
    fun fromDomainToApi(domain: SessionRq): ApiSessionRq
}