package com.waveneuro.ui.session.history

import com.waveneuro.domain.model.session.SessionListRs

sealed class SessionHistoryViewEffect {
    data class Success(val sessionList: SessionListRs) : SessionHistoryViewEffect()
}