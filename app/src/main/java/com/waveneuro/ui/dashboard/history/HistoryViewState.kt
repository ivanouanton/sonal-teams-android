package com.waveneuro.ui.dashboard.history

import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel

sealed class HistoryViewState {
    class Loading(val loading: Boolean) : HistoryViewState()
    class Success(val data: BaseModel) : HistoryViewState()
    class Failure(val error: BaseError) : HistoryViewState()
}