package com.waveneuro.ui.introduction.splash

sealed class SplashViewEffect {
    object Login : SplashViewEffect()
    object Home : SplashViewEffect()
}