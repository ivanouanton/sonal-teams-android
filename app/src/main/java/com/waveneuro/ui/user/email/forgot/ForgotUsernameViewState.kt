package com.waveneuro.ui.user.email.forgot

import com.asif.abase.data.model.BaseError
import com.waveneuro.data.model.entity.User

sealed class ForgotUsernameViewState {
    class Loading(val loading: Boolean) : ForgotUsernameViewState()
    class Success(val item: User) : ForgotUsernameViewState()
    class Failure(val error: BaseError) : ForgotUsernameViewState()
}