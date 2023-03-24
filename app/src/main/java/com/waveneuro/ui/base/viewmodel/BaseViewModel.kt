package com.waveneuro.ui.base.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.waveneuro.R
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.data.preference.PreferenceManagerImpl
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.handler.error.model.AppError
import com.waveneuro.ui.base.handler.error.model.FailedRefreshTokenError
import com.waveneuro.ui.base.handler.error.model.NoInternetError
import com.waveneuro.ui.base.utils.Event
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface BaseViewModel {
    val isLoading: LiveData<Boolean>
    val message: LiveData<Event<String?>>
    val logoutDone: LiveData<Event<Unit>>
}

abstract class BaseAndroidViewModelImpl(
    app: Application,
    private val errorHandler: ErrorHandler,
) : AndroidViewModel(app), BaseViewModel {

    val appCtx: Context get() = getApplication()

    private var pref: PreferenceManager = PreferenceManagerImpl(appCtx)

    override val isLoading = MutableLiveData<Boolean>()
    override val message = MutableLiveData<Event<String?>>()
    override val logoutDone = MutableLiveData<Event<Unit>>()

    protected fun launchPayload(
        showLoading: Boolean = true,
        customErrorConsumer: ((AppError) -> Boolean)? = null,
        showAnyError: Boolean = false,
        action: suspend () -> Unit
    ): Job = viewModelScope.launch {
        if (showLoading) {
            isLoading.value = true
        }

        try {
            action()
        } catch (e: CancellationException) {
            // bypass
            throw e
        } catch (e: Exception) {
            errorHandler.handle(e).let { appError ->
                if (showAnyError) {
                    if (customErrorConsumer?.invoke(appError) != true) {
                        message.value = Event(appCtx.getString(R.string.default_error_message))
                    }
                } else {
                    when (appError) {
                        NoInternetError -> onNoInternet()
                        FailedRefreshTokenError -> performLocalLogout()
                        is UnknownError -> {
                            message.value = Event(appCtx.getString(R.string.default_error_message))
                        }
                        else -> if (customErrorConsumer?.invoke(appError) != true) {
                            message.value = Event(appCtx.getString(R.string.default_error_message))
                        }
                    }
                }
            }
        } finally {
            if (showLoading) {
                isLoading.value = false
            }
        }
    }

    private fun performLocalLogout() {
        launchPayload {
            pref.logout()
            logoutDone.value = Event(Unit)
        }
    }

    private fun onNoInternet() {
        message.value = Event(appCtx.getString(R.string.no_internet_connection))
    }

}
