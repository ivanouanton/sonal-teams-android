package com.waveneuro.domain.usecase.password

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.api.user.model.password.ApiForgotPasswordRq
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val serviceApi: UserApi,
) {

    suspend fun forgotPassword(email: String) {
        serviceApi.forgotPassword(ApiForgotPasswordRq(email))
    }

}