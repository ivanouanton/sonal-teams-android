package com.waveneuro.ui.dashboard.device.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DeviceViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<DeviceViewModelImpl>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        DeviceViewModelImpl::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}