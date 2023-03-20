package com.waveneuro.domain.usecase.user

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.user.ApiUserUpdateRq
import com.waveneuro.domain.model.user.UserInfo
import com.waveneuro.domain.model.user.UserMapperImpl
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapperImpl
) {

    suspend fun updateUser(firstName: String, lastName: String): UserInfo =
        mapper.fromApiToDomain(userApi.updateUser(ApiUserUpdateRq(firstName, lastName)))

}