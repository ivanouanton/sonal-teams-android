package com.waveneuro.domain.usecase.user

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.user.UserInfo
import com.waveneuro.domain.model.user.UserMapperImpl
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapperImpl
) {

    suspend fun getUser(): UserInfo =
        mapper.fromApiToDomain(userApi.getUserInfo())

}