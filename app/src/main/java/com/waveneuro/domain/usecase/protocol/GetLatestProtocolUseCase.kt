package com.waveneuro.domain.usecase.protocol

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.protocol.ProtocolMapperImpl
import com.waveneuro.domain.model.protocol.ProtocolRs
import javax.inject.Inject

class GetLatestProtocolUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ProtocolMapperImpl
) {

    suspend fun getProtocol(id: Int): ProtocolRs =
        mapper.fromApiToDomain(userApi.getProtocolForUser(id))

}