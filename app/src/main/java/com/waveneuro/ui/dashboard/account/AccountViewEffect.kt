package com.waveneuro.ui.dashboard.account

sealed class AccountViewEffect {
    object BackRedirect : AccountViewEffect()
    object UpdateSuccess : AccountViewEffect()
}