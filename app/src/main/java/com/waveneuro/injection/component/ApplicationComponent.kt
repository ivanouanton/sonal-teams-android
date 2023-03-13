package com.waveneuro.injection.component

import android.content.Context
import com.asif.abase.injection.qualifier.ApplicationContext
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.injection.module.ApplicationModule
import com.waveneuro.ui.introduction.splash.viewmodel.SplashViewModelFactory
import com.waveneuro.ui.user.login.viewmodel.LoginViewModelFactory
import com.waveneuro.ui.user.mfa.viewmodel.MfaViewModelFactory
import com.waveneuro.ui.user.password.code.viewmodel.ForgotPasswordCodeViewModelFactory
import com.waveneuro.ui.user.password.new_password.viewmodel.SetNewPasswordViewModelFactory
import com.waveneuro.ui.user.password.reset.viewmodel.ResetPasswordViewModelFactory
import com.waveneuro.ui.user.registration.viewmodel.RegistrationViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context
    fun dataManager(): DataManager
    fun preferenceManager(): PreferenceManager
    fun analyticsManager(): AnalyticsManager

    // ViewModels
    fun splashViewModelFactory(): SplashViewModelFactory
    fun registrationViewModelFactory(): RegistrationViewModelFactory
    fun resetPasswordViewModelFactory(): ResetPasswordViewModelFactory
    fun loginViewModelFactory(): LoginViewModelFactory
    fun setNewPasswordViewModelFactory(): SetNewPasswordViewModelFactory
    fun forgotPasswordCodeViewModelFactory(): ForgotPasswordCodeViewModelFactory
    fun mfaViewModelFactory(): MfaViewModelFactory

}