package com.waveneuro.domain.usecase.client

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.client.ClientMapperImpl
import com.waveneuro.domain.model.client.ClientRs
import com.waveneuro.domain.model.user.UserInfo
import com.waveneuro.domain.model.user.UserMapperImpl
import javax.inject.Inject

class GetClientUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ClientMapperImpl
) {

    suspend fun getClient(id: Int): ClientRs =
        mapper.fromApiToDomain(userApi.getClient(id))

}