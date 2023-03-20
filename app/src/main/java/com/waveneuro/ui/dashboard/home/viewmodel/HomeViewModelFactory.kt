package com.waveneuro.ui.dashboard.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class HomeViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<HomeViewModelImpl>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        HomeViewModelImpl::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}