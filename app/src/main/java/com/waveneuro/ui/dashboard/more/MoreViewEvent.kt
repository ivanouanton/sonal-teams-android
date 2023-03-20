package com.waveneuro.ui.dashboard.more

sealed class MoreViewEvent {
    object ProfileInfoClicked : MoreViewEvent()
    object DeviceHistoryClicked : MoreViewEvent()
    object HelpClicked : MoreViewEvent()
    object LogoutClicked : MoreViewEvent()
}