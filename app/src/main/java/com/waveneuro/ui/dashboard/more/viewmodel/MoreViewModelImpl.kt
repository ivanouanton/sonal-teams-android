package com.waveneuro.ui.dashboard.more.viewmodel

import android.app.Application
import com.waveneuro.data.analytics.AnalyticsEvent
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.preference.PreferenceManagerImpl
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.more.MoreViewEffect
import com.waveneuro.ui.dashboard.more.MoreViewEffect.*
import com.waveneuro.ui.dashboard.more.MoreViewEvent
import com.waveneuro.ui.dashboard.more.MoreViewEvent.*
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class MoreViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val analyticsManager: AnalyticsManager,
) : BaseAndroidViewModelImpl(app, errorHandler), MoreViewModel {

    private val prefs = PreferenceManagerImpl(appCtx)

    override val viewEffect = SingleLiveEvent<MoreViewEffect>()

    override fun processEvent(viewEvent: MoreViewEvent) {
        when (viewEvent) {
            is ProfileInfoClicked -> viewEffect.postValue(ProfileInfo)
            is DeviceHistoryClicked -> viewEffect.postValue(DeviceHistory)
            is HelpClicked -> viewEffect.postValue(Help)
            is LogoutClicked -> {
                sentLogoutEvent(prefs.userId ?: "")
                logout()
                viewEffect.postValue(Login)
            }
        }
    }

    private fun sentLogoutEvent(userId: String) {
        val properties = JSONObject()
        try {
            properties.put("user_id", userId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        analyticsManager.sendEvent(AnalyticsEvent.LOGOUT, properties, AnalyticsManager.MIX_PANEL)
    }

    private fun logout() {
        prefs.logout()
    }

}