package com.waveneuro.ui.dashboard.account.viewmodel

import android.app.Application
import com.waveneuro.data.preference.PreferenceManagerImpl
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.user.GetUserInfoUseCase
import com.waveneuro.domain.usecase.user.UpdateUserInfoUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.account.AccountViewEffect
import com.waveneuro.ui.dashboard.account.AccountViewEffect.UpdateSuccess
import com.waveneuro.ui.dashboard.account.AccountViewEvent
import com.waveneuro.ui.dashboard.account.AccountViewEvent.UpdatedUser
import javax.inject.Inject

class AccountViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), AccountViewModel {

    private val prefs = PreferenceManagerImpl(appCtx)

    override val viewEffect = SingleLiveEvent<AccountViewEffect>()

    override fun processEvent(viewEvent: AccountViewEvent) {
        when (viewEvent) {
            is AccountViewEvent.Start -> getUser()
            is UpdatedUser -> updateUser(viewEvent.firstName, viewEvent.lastName)
            is AccountViewEvent.BackClicked -> {
                viewEffect.postValue(AccountViewEffect.BackRedirect)
            }
        }
    }

    private fun getUser() {
        launchPayload {
            val response = getUserInfoUseCase.getUser()

            prefs.saveUser(response)
            viewEffect.postValue(AccountViewEffect.GetSuccess(response))
        }
    }

    private fun updateUser(firstName: String, lastName: String) {
        launchPayload {
            val response = updateUserInfoUseCase.updateUser(firstName, lastName)

            prefs.saveUser(response)
            viewEffect.postValue(UpdateSuccess(response))
        }
    }

}