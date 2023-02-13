package com.waveneuro.ui.dashboard.history

import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel

sealed class HistoryViewState {
    data class Loading(val loading: Boolean) : HistoryViewState()
    data class Success(val data: BaseModel) : HistoryViewState()
    data class Failure(val error: BaseError) : HistoryViewState()
}