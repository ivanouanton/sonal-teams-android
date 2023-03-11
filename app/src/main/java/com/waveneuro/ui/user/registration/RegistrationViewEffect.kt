package com.waveneuro.ui.user.registration

sealed class RegistrationViewEffect {
    object Back : RegistrationViewEffect()
    object BookConsultation : RegistrationViewEffect()
    object FindOutMore : RegistrationViewEffect()
}