package com.waveneuro.ui.user.password.recovery

sealed class RecoveryInstructionsViewEvent {
    class BackClicked : RecoveryInstructionsViewEvent()
    class ContactUsClicked : RecoveryInstructionsViewEvent()
    class OpenEmailAppClicked : RecoveryInstructionsViewEvent()
    class SkipClicked : RecoveryInstructionsViewEvent()
    class Start : RecoveryInstructionsViewEvent()
}