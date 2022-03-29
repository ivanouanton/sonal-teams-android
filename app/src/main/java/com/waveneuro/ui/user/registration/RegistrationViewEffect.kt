package com.waveneuro.ui.user.registration

sealed class RegistrationViewEffect {
    class Back : RegistrationViewEffect()
    class BookConsultation : RegistrationViewEffect()
    class FindOutMore : RegistrationViewEffect()
}