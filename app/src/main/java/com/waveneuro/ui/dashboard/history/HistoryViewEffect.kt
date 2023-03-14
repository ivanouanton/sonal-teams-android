package com.waveneuro.ui.dashboard.history

import com.waveneuro.domain.model.device.Device

sealed class HistoryViewEffect {
    data class Success(val deviceList: List<Device>) : HistoryViewEffect()
}