package com.waveneuro.ui.user.password.recovery

sealed class RecoveryInstructionsViewEffect {
    class Back : RecoveryInstructionsViewEffect()
    class ContactUs : RecoveryInstructionsViewEffect()
    class EmailApp : RecoveryInstructionsViewEffect()
    class Skip : RecoveryInstructionsViewEffect()
}