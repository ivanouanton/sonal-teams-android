package com.waveneuro.ui.dashboard.home

import com.asif.abase.data.model.BaseError
import com.waveneuro.data.model.response.protocol.ProtocolResponse

sealed class HomeProtocolViewState {
    class Loading(val loading: Boolean) : HomeProtocolViewState()
    class Success(val item: ProtocolResponse) : HomeProtocolViewState()
    class Failure(val error: BaseError) : HomeProtocolViewState()
}