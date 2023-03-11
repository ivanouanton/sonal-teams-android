package com.waveneuro.ui.user.login

import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel

sealed class LoginViewState {
    class Success(val item: BaseModel) : LoginViewState()
    class Failure(val error: BaseError) : LoginViewState()
}