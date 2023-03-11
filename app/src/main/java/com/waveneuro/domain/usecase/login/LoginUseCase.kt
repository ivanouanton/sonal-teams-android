package com.waveneuro.domain.usecase.login

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.login.ApiLoginRq
import com.waveneuro.domain.model.login.LoginMapperImpl
import com.waveneuro.domain.model.login.LoginRsMfa
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val serviceApi: UserApi,
    private val mapper: LoginMapperImpl
) {

    suspend fun login(username: String, password: String): LoginRsMfa =
        mapper.fromApiToDomain(serviceApi.login(ApiLoginRq(username, password)))

}