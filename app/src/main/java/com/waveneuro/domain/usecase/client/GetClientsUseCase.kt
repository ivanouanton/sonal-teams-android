package com.waveneuro.domain.usecase.client

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.client.ClientListRs
import com.waveneuro.domain.model.client.ClientMapperImpl
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ClientMapperImpl
) {

    suspend fun getClients(newPage: Int, newQuery: String, newIds: List<Int>): ClientListRs =
        mapper.fromApiToDomain(userApi.getClientList(newPage, newQuery, newIds))

}