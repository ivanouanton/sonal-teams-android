package com.waveneuro.ui.dashboard.account

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.asif.abase.exception.SomethingWrongException
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest
import com.waveneuro.data.model.response.user.UserInfoResponse
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase
import com.waveneuro.domain.usecase.user.UpdatePersonalInfoUseCase
import com.waveneuro.ui.dashboard.account.AccountViewEffect.UpdateSuccess
import com.waveneuro.ui.dashboard.account.AccountViewEvent.UpdatedUser
import com.waveneuro.utils.ErrorUtil
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val updatePersonalInfoUseCase: UpdatePersonalInfoUseCase
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager

    val data = MutableLiveData<AccountViewState>()
    val viewEffect = SingleLiveEvent<AccountViewEffect>()

    fun processEvent(viewEvent: AccountViewEvent?) {
        when (viewEvent) {
            is AccountViewEvent.Start -> getPersonalInfo()
            is UpdatedUser -> updateUser(viewEvent.firstName, viewEvent.lastName)
            is AccountViewEvent.BackClicked -> {
                viewEffect.postValue(AccountViewEffect.BackRedirect)
            }
            else -> {}
        }
    }

    private fun updateUser(firstName: String, lastName: String) {
        val accountUpdateRequest = AccountUpdateRequest()
        accountUpdateRequest.firstName = firstName
        accountUpdateRequest.lastName = lastName
        data.postValue(AccountViewState.Loading(true))
        updatePersonalInfoUseCase.execute(accountUpdateRequest, object : UseCaseCallback<UserInfoResponse> {
            override fun onSuccess(response: UserInfoResponse) {
                data.postValue(AccountViewState.Loading(false))
                if (response.error != null && TextUtils.isEmpty(response.error)) {
                    data.postValue(AccountViewState.Loading(false))
                    val error = errorUtil.parseError(
                        SomethingWrongException(),
                        response.error
                    )
                    data.postValue(AccountViewState.Failure(error))
                } else {
                    dataManager.saveUser(response)
                    data.setValue(AccountViewState.Success(response))
                }

                viewEffect.postValue(UpdateSuccess)
            }

            override fun onError(throwable: Throwable) {
                data.postValue(AccountViewState.Loading(false))
                val error = errorUtil.parseError(throwable)
                data.postValue(AccountViewState.Failure(error))
            }

            override fun onFinish() {}
        })
    }

    private fun getPersonalInfo() {
        data.postValue(AccountViewState.Loading(true))
        getPersonalInfoUseCase.execute(object : UseCaseCallback<UserInfoResponse> {
            override fun onSuccess(response: UserInfoResponse) {
                data.postValue(AccountViewState.Loading(false))
                if (response.error != null && TextUtils.isEmpty(response.error)) {
                    data.postValue(AccountViewState.Loading(false))
                    val error = errorUtil.parseError(
                        SomethingWrongException(),
                        response.error
                    )
                    data.postValue(AccountViewState.Failure(error))
                } else {
                    dataManager.saveUser(response)
                    data.setValue(AccountViewState.Success(response))
                }
            }

            override fun onError(throwable: Throwable) {
                data.postValue(AccountViewState.Loading(false))
                val error = errorUtil.parseError(throwable)
                data.postValue(AccountViewState.Failure(error))
            }

            override fun onFinish() {}
        })
    }

}