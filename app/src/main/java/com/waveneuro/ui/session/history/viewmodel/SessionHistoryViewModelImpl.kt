package com.waveneuro.ui.session.history.viewmodel

import android.app.Application
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.session.GetSessionHistoryUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.session.history.SessionHistoryViewEffect
import com.waveneuro.ui.session.history.SessionHistoryViewEvent
import javax.inject.Inject

class SessionHistoryViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val getSessionHistoryUseCase: GetSessionHistoryUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), SessionHistoryViewModel {

    override val viewEffect = SingleLiveEvent<SessionHistoryViewEffect>()

    override fun processEvent(viewEvent: SessionHistoryViewEvent) {
        when(viewEvent) {
            is SessionHistoryViewEvent.Start -> {
                getSessionHistory(viewEvent.id)
            }
        }
    }

    private fun getSessionHistory(id: Int) {
        launchPayload {
            val response = getSessionHistoryUseCase.getSessions(id)
            viewEffect.postValue(SessionHistoryViewEffect.Success(response))
        }
    }

}