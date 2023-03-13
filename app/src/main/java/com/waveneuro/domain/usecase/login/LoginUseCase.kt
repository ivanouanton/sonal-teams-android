package com.waveneuro.domain.usecase.login

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.login.ApiLoginRq
import com.waveneuro.domain.model.login.LoginMapperImpl
import com.waveneuro.domain.model.login.LoginMfaRs
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: LoginMapperImpl
) {

    suspend fun login(username: String, password: String): LoginMfaRs =
        mapper.fromApiToDomain(userApi.login(ApiLoginRq(username, password)))

}