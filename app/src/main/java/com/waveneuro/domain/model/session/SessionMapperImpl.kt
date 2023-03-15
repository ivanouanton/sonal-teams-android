package com.waveneuro.domain.model.session

import com.waveneuro.data.api.user.model.session.ApiSessionRq
import javax.inject.Inject

class SessionMapperImpl @Inject constructor() : SessionMapper {

    override fun fromDomainToApi(domain: SessionRq): ApiSessionRq = with(domain) {
        ApiSessionRq(
            eegId ?: 0,
            finishedAt ?: 0,
            isCompleted,
            patientId ?: 0,
            protocolId ?: 0,
            sonalId ?: ""
        )
    }

}