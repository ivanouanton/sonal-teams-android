package com.waveneuro.ui.session.history

import com.asif.abase.domain.base.ObservableUseCase
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.response.session.SessionResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetSessionHistoryUseCase @Inject constructor(
    private val dataManager: DataManager
) : ObservableUseCase<SessionResponse>() {

    private var id = 0

    override fun buildUseCaseSingle(): Observable<SessionResponse> {
        return dataManager.getSessions(id)
    }

    fun execute(id: Int, useCaseCallback: UseCaseCallback<SessionResponse?>?) {
        this.id = id
        super.execute(useCaseCallback)
    }

}