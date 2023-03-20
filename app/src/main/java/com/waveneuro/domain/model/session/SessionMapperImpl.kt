package com.waveneuro.domain.model.session

import com.waveneuro.data.api.session.model.session.ApiSession
import com.waveneuro.data.api.session.model.session.ApiSessionListRs
import com.waveneuro.data.api.session.model.session.ApiSessionRq
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

    override fun fromApiToDomain(api: ApiSessionListRs): SessionListRs = with(api) {
        SessionListRs(
            sessions.map(::fromApiToDomain)
        )
    }

    private fun fromApiToDomain(api: ApiSession): Session = with(api) {
        Session(
            eegRecordedAt, finishedAt, isCompleted, sonalId
        )
    }

}