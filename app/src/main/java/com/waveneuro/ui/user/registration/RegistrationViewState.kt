package com.waveneuro.ui.user.registration

import com.asif.abase.data.model.BaseError
import com.waveneuro.data.model.entity.User

sealed class RegistrationViewState {
    class Loading(val loading: Boolean) : RegistrationViewState()
    class Success(val item: User) : RegistrationViewState()
    class Failure(val error: BaseError) : RegistrationViewState()
}