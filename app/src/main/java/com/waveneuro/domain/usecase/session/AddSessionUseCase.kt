package com.waveneuro.domain.usecase.session

import com.waveneuro.data.api.session.SessionApi
import com.waveneuro.domain.model.session.SessionMapperImpl
import com.waveneuro.domain.model.session.SessionRq
import javax.inject.Inject

class AddSessionUseCase @Inject constructor(
    private val sessionApi: SessionApi,
    private val mapper: SessionMapperImpl
) {

    suspend fun addSession(request: SessionRq) =
        sessionApi.addSession(mapper.fromDomainToApi(request))

}