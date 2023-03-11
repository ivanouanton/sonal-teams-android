package com.waveneuro.injection.component;

import android.content.Context;

import com.asif.abase.injection.qualifier.ApplicationContext;
import com.asif.abase.injection.scope.PerActivity;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.injection.module.ActivityModule;
import com.waveneuro.ui.dashboard.DashboardActivity;
import com.waveneuro.ui.dashboard.account.AccountActivity;
import com.waveneuro.ui.dashboard.help.HelpActivity;
import com.waveneuro.ui.dashboard.history.HistoryActivity;
import com.waveneuro.ui.dashboard.organization.OrganizationActivity;
import com.waveneuro.ui.dashboard.web.WebActivity;
import com.waveneuro.ui.device.MyDeviceActivity;
import com.waveneuro.ui.introduction.splash.SplashActivity;
import com.waveneuro.ui.session.complete.SessionCompleteActivity;
import com.waveneuro.ui.session.history.SessionHistoryActivity;
import com.waveneuro.ui.session.how_to.HowToActivity;
import com.waveneuro.ui.session.session.SessionActivity;
import com.waveneuro.ui.user.login.LoginActivity;
import com.waveneuro.ui.user.mfa.MfaActivity;
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeActivity;
import com.waveneuro.ui.user.password.new_password.SetNewPasswordActivity;
import com.waveneuro.ui.user.password.reset.ResetPasswordActivity;
import com.waveneuro.ui.user.registration.RegistrationActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    @ApplicationContext
    Context context();

    DataManager dataManager();

    PreferenceManager preferenceManager();

    AnalyticsManager analyticsManager();

    void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(RegistrationActivity registrationActivity);

    void inject(ResetPasswordActivity resetPasswordActivity);

    void inject(DashboardActivity dashboardActivity);

    void inject(SessionActivity sessionActivity);

    void inject(SessionCompleteActivity sessionCompleteActivity);

    void inject(WebActivity webActivity);

    void inject(AccountActivity accountActivity);

    void inject(OrganizationActivity organizationActivity);

    void inject(SetNewPasswordActivity setNewPasswordActivity);

    void inject(MyDeviceActivity deviceActivity);

    void inject(HelpActivity helpActivity);

    void inject(HistoryActivity helpActivity);

    void inject(MfaActivity helpActivity);

    void inject(SessionHistoryActivity sessionHistoryActivity);

    void inject(HowToActivity howToActivity);

    void inject(ForgotPasswordCodeActivity forgotPasswordCodeActivity);
}
