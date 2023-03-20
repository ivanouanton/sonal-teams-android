package com.waveneuro.domain.usecase.client

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.client.ClientMapperImpl
import com.waveneuro.domain.model.client.ClientRq
import com.waveneuro.domain.model.client.ClientRs
import javax.inject.Inject

class UpdateClientUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ClientMapperImpl
) {

    suspend fun updateClient(id: Int, clientRq: ClientRq): ClientRs =
        mapper.fromApiToDomain(userApi.updateClient(id, mapper.fromDomainToApi(clientRq)))

}