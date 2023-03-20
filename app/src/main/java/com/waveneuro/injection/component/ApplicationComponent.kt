package com.waveneuro.injection.component

import android.content.Context
import com.asif.abase.injection.qualifier.ApplicationContext
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.injection.module.ApiModule
import com.waveneuro.injection.module.ApplicationModule
import com.waveneuro.ui.dashboard.DashboardViewModelFactory
import com.waveneuro.ui.dashboard.account.viewmodel.AccountViewModelFactory
import com.waveneuro.ui.dashboard.device.viewmodel.DeviceViewModelFactory
import com.waveneuro.ui.dashboard.history.viewmodel.HistoryViewModelFactory
import com.waveneuro.ui.dashboard.home.viewmodel.HomeViewModelFactory
import com.waveneuro.ui.dashboard.more.viewmodel.MoreViewModelFactory
import com.waveneuro.ui.dashboard.organization.viewmodel.OrganizationViewModelFactory
import com.waveneuro.ui.introduction.splash.viewmodel.SplashViewModelFactory
import com.waveneuro.ui.session.history.viewmodel.SessionHistoryViewModelFactory
import com.waveneuro.ui.session.session.viewmodel.SessionViewModelFactory
import com.waveneuro.ui.user.login.viewmodel.LoginViewModelFactory
import com.waveneuro.ui.user.mfa.viewmodel.MfaViewModelFactory
import com.waveneuro.ui.user.password.code.viewmodel.ForgotPasswordCodeViewModelFactory
import com.waveneuro.ui.user.password.new_password.viewmodel.SetNewPasswordViewModelFactory
import com.waveneuro.ui.user.password.reset.viewmodel.ResetPasswordViewModelFactory
import com.waveneuro.ui.user.registration.viewmodel.RegistrationViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ApiModule::class,])
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
    fun dashboardViewModelFactory(): DashboardViewModelFactory
    fun organizationViewModelFactory(): OrganizationViewModelFactory
    fun historyViewModelFactory(): HistoryViewModelFactory
    fun accountViewModelFactory(): AccountViewModelFactory
    fun sessionViewModelFactory(): SessionViewModelFactory
    fun sessionHistoryViewModelFactory(): SessionHistoryViewModelFactory
    fun deviceViewModelFactory(): DeviceViewModelFactory
    fun moreViewModelFactory(): MoreViewModelFactory
    fun homeViewModelFactory(): HomeViewModelFactory

}