package com.waveneuro.ui.introduction.splash;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;

import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;
import com.waveneuro.ui.user.login.LoginCommand;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {


    @Inject
    LoginCommand loginCommand;
    @Inject
    DashboardCommand dashboardCommand;

    @Inject
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        activityComponent().inject(this);
        setContentView(R.layout.activity_splash);
        setView();
        setObserver();

        this.splashViewModel.processEvent(new SplashViewEvent.Start());
    }

    private void setView() {

    }

    private void setObserver() {
        this.splashViewModel.getData().observe(this, splashViewStateObserver);
        this.splashViewModel.getViewEffect().observe(this, splashViewEffectObserver);
    }

    Observer<SplashViewState> splashViewStateObserver = viewState -> {

    };

    Observer<SplashViewEffect> splashViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof SplashViewEffect.Login) {
            launchLoginScreen();
        } else if (viewEffect instanceof SplashViewEffect.Home) {
            launchHomeScreen();
        }
    };


    private void launchLoginScreen() {
        loginCommand.navigate();
    }

    private void launchHomeScreen() {
        dashboardCommand.navigate();
    }
}