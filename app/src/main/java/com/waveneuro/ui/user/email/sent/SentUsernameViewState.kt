package com.waveneuro.ui.user.email.sent

import com.waveneuro.data.model.entity.User

sealed class SentUsernameViewState {
    class Loading(val loading: Boolean) : SentUsernameViewState()
    class Success(val item: User) : SentUsernameViewState()
    class Failure(val error: Error) : SentUsernameViewState()
}