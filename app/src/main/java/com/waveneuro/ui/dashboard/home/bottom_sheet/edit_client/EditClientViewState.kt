package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client


sealed class EditClientViewState {
    object Success: EditClientViewState()
    data class Error(val message: String?) : EditClientViewState()
}