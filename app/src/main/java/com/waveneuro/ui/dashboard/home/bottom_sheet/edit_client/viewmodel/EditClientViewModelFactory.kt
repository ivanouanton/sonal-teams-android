package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class EditClientViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<EditClientViewModelImpl>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        EditClientViewModelImpl::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]?.get() as T
    }

}