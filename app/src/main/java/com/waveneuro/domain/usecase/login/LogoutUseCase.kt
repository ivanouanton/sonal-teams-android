package com.waveneuro.domain.usecase.login

import com.waveneuro.data.api.user.UserApi
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val serviceApi: UserApi,
) {

    suspend fun logout() =
        serviceApi.logout()
}
