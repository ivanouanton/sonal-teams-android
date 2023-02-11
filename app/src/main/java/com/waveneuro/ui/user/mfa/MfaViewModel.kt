package com.waveneuro.ui.user.mfa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.model.request.login.ConfirmTokenRequest
import com.waveneuro.data.model.response.login.ConfirmTokenResponse
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.login.ConfirmTokenUseCase
import com.waveneuro.ui.user.login.LoginViewEffect
import com.waveneuro.ui.user.login.LoginViewState
import com.waveneuro.utils.ErrorUtil
import javax.inject.Inject

class MfaViewModel @Inject constructor(
    private val confirmTokenUseCase: ConfirmTokenUseCase
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var preferenceManager: PreferenceManager
    @Inject
    lateinit var analyticsManager: AnalyticsManager

    val data = MutableLiveData<LoginViewState>()
    val viewEffect = SingleLiveEvent<LoginViewEffect>()

    fun confirmToken(mfaCode: String?, username: String?, session: String?) {
        confirmTokenUseCase.execute(
            ConfirmTokenRequest(username, mfaCode, session),
            object : UseCaseCallback<ConfirmTokenResponse> {
                override fun onSuccess(confirmTokenResponse: ConfirmTokenResponse) {
                    viewEffect.postValue(LoginViewEffect.Home)
                    preferenceManager.accessToken = confirmTokenResponse.idToken
                }

                override fun onError(throwable: Throwable) {
                    viewEffect.postValue(LoginViewEffect.WrongMfaCode)
                }

                override fun onFinish() {}
            })
    }

}