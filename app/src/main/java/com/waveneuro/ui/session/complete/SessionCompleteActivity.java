package com.waveneuro.ui.session.complete;

import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionCompleteActivity extends BaseActivity {

    @Inject
    DashboardCommand dashboardCommand;

    @Inject
    SessionCompleteViewModel sessionCompleteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_session_complete);
        ButterKnife.bind(this);

        setView();
        setObserver();
        this.sessionCompleteViewModel.processEvent(new SessionCompleteViewEvent.Start());
    }

    private void setView() {
        this.sessionCompleteViewModel.processEvent(new SessionCompleteViewEvent.Start());
    }

    private void setObserver() {
        this.sessionCompleteViewModel.getData().observe(this, sessionCompleteViewStateObserver);
        this.sessionCompleteViewModel.getViewEffect().observe(this, sessionCompleteViewEffectObserver);
    }

    Observer<SessionCompleteViewState> sessionCompleteViewStateObserver = viewState -> {
        if (viewState instanceof SessionCompleteViewState.Complete) {
        }
    };

    Observer<SessionCompleteViewEffect> sessionCompleteViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof SessionCompleteViewEffect.Back) {
        } else if (viewEffect instanceof SessionCompleteViewEffect.Home) {
            launchHomeScreen();
        }
    };

    private void launchHomeScreen() {
        this.dashboardCommand.navigate();
    }

    @OnClick(R.id.btn_go_home)
    public void onClickGoHome() {
        this.sessionCompleteViewModel.processEvent(new SessionCompleteViewEvent.HomeClicked());
    }

    @Override
    public void onBackPressed() {
        this.dashboardCommand.navigate();
    }
}