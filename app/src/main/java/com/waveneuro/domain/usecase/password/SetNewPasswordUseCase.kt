package com.waveneuro.domain.usecase.password

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRq
import javax.inject.Inject

class SetNewPasswordUseCase @Inject constructor(
    private val userApi: UserApi,
) {

    suspend fun setNewPassword(email: String, code: String, password: String) {
        userApi.changeTempPassword(ApiSetNewPasswordRq(email, code, password))
    }

}
