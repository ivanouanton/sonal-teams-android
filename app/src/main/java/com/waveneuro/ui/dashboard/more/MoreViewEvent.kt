package com.waveneuro.ui.dashboard.more

internal sealed class MoreViewEvent {
    object ProfileInfoClicked : MoreViewEvent()
    object DeviceHistoryClicked : MoreViewEvent()
    object HelpClicked : MoreViewEvent()
    object LogoutClicked : MoreViewEvent()
}