package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client


sealed class EditClientViewState {
    data class Success(val fullName: String?): EditClientViewState()
}