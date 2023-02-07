package com.waveneuro.ui.dashboard.more

internal sealed class MoreViewEffect {
    object ProfileInfo : MoreViewEffect()
    object DeviceHistory : MoreViewEffect()
    object Help : MoreViewEffect()
    object Login : MoreViewEffect()
}