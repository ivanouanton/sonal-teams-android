package com.waveneuro.ui.dashboard.more

import androidx.lifecycle.ViewModel
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsEvent
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.dashboard.more.MoreViewEvent.*
import com.waveneuro.ui.dashboard.more.MoreViewEffect.*
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

internal class MoreViewModel @Inject constructor(
    val analyticsManager: AnalyticsManager,
    val dataManager: DataManager
) : ViewModel() {

    val viewEffect = SingleLiveEvent<MoreViewEffect>()

    fun processEvent(viewEvent: MoreViewEvent?) {
        when (viewEvent) {
            is ProfileInfoClicked -> viewEffect.postValue(ProfileInfo)
            is DeviceHistoryClicked -> viewEffect.postValue(DeviceHistory)
            is HelpClicked -> viewEffect.postValue(Help)
            is LogoutClicked -> {
                sentLogoutEvent(dataManager.user.id)
                logout()
                viewEffect.postValue(Login)
            }
            else -> {}
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
        dataManager.logout()
    }

}