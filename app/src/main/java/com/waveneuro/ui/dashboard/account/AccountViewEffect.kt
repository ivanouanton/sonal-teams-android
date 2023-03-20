package com.waveneuro.ui.dashboard.account

import com.waveneuro.domain.model.user.UserInfo

sealed class AccountViewEffect {
    object BackRedirect : AccountViewEffect()
    data class GetSuccess(val user: UserInfo) : AccountViewEffect()
    data class UpdateSuccess(val user: UserInfo) : AccountViewEffect()
}