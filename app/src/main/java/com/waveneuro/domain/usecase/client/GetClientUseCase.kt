package com.waveneuro.domain.usecase.client

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.client.ClientInfo
import com.waveneuro.domain.model.client.ClientMapperImpl
import javax.inject.Inject

class GetClientUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ClientMapperImpl
) {

    suspend fun getClient(id: Int): ClientInfo =
        mapper.fromApiToDomain(userApi.getClient(id))

}