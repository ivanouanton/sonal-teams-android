package com.waveneuro.domain.usecase.password

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.password.ApiSetNewPasswordRq
import com.waveneuro.domain.model.password.PasswordMapperImpl
import com.waveneuro.domain.model.password.SetNewPasswordRs
import javax.inject.Inject

class SetNewPasswordUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: PasswordMapperImpl
) {

    suspend fun setNewPassword(email: String, code: String, password: String): SetNewPasswordRs =
        mapper.fromApiToDomain(userApi.changeTempPassword(ApiSetNewPasswordRq(email, code, password)))

}