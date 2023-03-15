package com.waveneuro.domain.usecase.user

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.client.ClientInfo
import com.waveneuro.domain.model.client.ClientMapperImpl
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ClientMapperImpl
) {

    suspend fun getUser(): ClientInfo =
        mapper.fromApiToDomain(userApi.getUserInfo())

}