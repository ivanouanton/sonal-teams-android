package com.waveneuro.ui.dashboard.account

import com.waveneuro.domain.model.client.ClientInfo

sealed class AccountViewEffect {
    object BackRedirect : AccountViewEffect()
    data class GetSuccess(val user: ClientInfo) : AccountViewEffect()
    data class UpdateSuccess(val user: ClientInfo) : AccountViewEffect()
}