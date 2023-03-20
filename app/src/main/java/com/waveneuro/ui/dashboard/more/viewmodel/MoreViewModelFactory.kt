package com.waveneuro.ui.dashboard.more.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class MoreViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<MoreViewModelImpl>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        MoreViewModelImpl::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}