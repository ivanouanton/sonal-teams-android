package com.waveneuro.ui.introduction.splash.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.introduction.splash.SplashViewEffect
import com.waveneuro.ui.introduction.splash.SplashViewEvent

interface SplashViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<SplashViewEffect>
    fun processEvent(viewEvent: SplashViewEvent)
}