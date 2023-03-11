package com.waveneuro.injection.component

import android.content.Context
import com.asif.abase.injection.qualifier.ApplicationContext
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.injection.module.ApplicationModule
import com.waveneuro.ui.user.login.viewmodel.LoginViewModelFactory
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
    fun registrationViewModelFactory(): RegistrationViewModelFactory
    fun resetPasswordViewModelFactory(): ResetPasswordViewModelFactory
    fun loginViewModelFactory(): LoginViewModelFactory

}