package com.waveneuro.ui.dashboard.account

sealed class AccountViewEffect {
    class BackRedirect : AccountViewEffect()
    class UpdateSuccess : AccountViewEffect()
}