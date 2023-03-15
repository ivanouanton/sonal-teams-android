package com.waveneuro.domain.usecase.user

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.user.ApiUserUpdateRq
import com.waveneuro.domain.model.client.ClientInfo
import com.waveneuro.domain.model.client.ClientMapperImpl
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: ClientMapperImpl
) {

    suspend fun updateUser(firstName: String, lastName: String): ClientInfo =
        mapper.fromApiToDomain(userApi.updateUser(ApiUserUpdateRq(firstName, lastName)))

}