package com.waveneuro.ui.dashboard.home

sealed class HomeViewEffect {
    object BackRedirect : HomeViewEffect()
    object DeviceRedirect : HomeViewEffect()
}