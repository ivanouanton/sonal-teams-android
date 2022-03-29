package com.waveneuro.ui.introduction.splash

sealed class SplashViewEffect {
    class Login : SplashViewEffect()
    class Home : SplashViewEffect()
}