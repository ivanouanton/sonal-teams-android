package com.waveneuro.ui.user.password.new_password.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class SetNewPasswordViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<SetNewPasswordViewModelImpl>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        SetNewPasswordViewModelImpl::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}