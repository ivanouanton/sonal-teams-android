package com.waveneuro.ui.user.registration

sealed class RegistrationViewEvent {
    class BackClicked : RegistrationViewEvent()
    class BookConsultationClicked : RegistrationViewEvent()
    class FindOutMoreClicked : RegistrationViewEvent()
    class Start : RegistrationViewEvent()
}