package com.waveneuro.ui.user.registration

sealed class RegistrationViewEvent {
    object BackClicked : RegistrationViewEvent()
    object BookConsultationClicked : RegistrationViewEvent()
    object FindOutMoreClicked : RegistrationViewEvent()
    object Start : RegistrationViewEvent()
}