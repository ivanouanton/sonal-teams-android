package com.waveneuro.ui.session.history

data class Session(
    val name: String,
    val rd: String?,
    val sd: String?,
    val isCompleted: Boolean
)