package com.waveneuro.ui.session.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.response.session.SessionResponse
import com.waveneuro.utils.ErrorUtil
import javax.inject.Inject

class SessionHistoryViewModel @Inject constructor(
    private val getSessionHistoryUseCase: GetSessionHistoryUseCase
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager

    val data = MutableLiveData<SessionResponse?>()

    fun getSessionHistory(id: Int) {
        getSessionHistoryUseCase.execute(id, object : UseCaseCallback<SessionResponse?> {
            override fun onSuccess(response: SessionResponse?) {
                data.postValue(response)
            }

            override fun onError(throwable: Throwable) {}

            override fun onFinish() {}
        })
    }
}