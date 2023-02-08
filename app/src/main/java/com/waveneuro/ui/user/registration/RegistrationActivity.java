package com.waveneuro.ui.user.registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.waveneuro.R;
import com.waveneuro.data.Config;
import com.waveneuro.ui.base.BaseFormActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends BaseFormActivity {

    @Inject
    RegistrationViewModel registrationViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
    }

    private void setObserver() {
        this.registrationViewModel.getData().observe(this, loginViewStateObserver);
        this.registrationViewModel.getViewEffect().observe(this, loginViewEffectObserver);
    }


    Observer<RegistrationViewState> loginViewStateObserver = viewState -> {

    };

    Observer<RegistrationViewEffect> loginViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof RegistrationViewEffect.Back) {
            goBack();
        } else if (viewEffect instanceof RegistrationViewEffect.BookConsultation) {
            launchBookConsultationScreen();
        } else if (viewEffect instanceof RegistrationViewEffect.FindOutMore) {
            launchFindOutMoreScreen();
        }
    };

    private void goBack() {
        onBackPressed();
        finish();
    }

    private void launchBookConsultationScreen() {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.BOOK_CONSULTATION_URL));
        startActivity(launchBrowser);
    }

    private void launchFindOutMoreScreen() {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.FIND_OUT_MORE_URL));
        startActivity(launchBrowser);
    }


    @OnClick(R.id.iv_back)
    public void onClickBack() {
        this.registrationViewModel.processEvent(new RegistrationViewEvent.BackClicked());
    }

    @OnClick(R.id.btn_book_consultation)
    public void onClickBookConsultation() {
        this.registrationViewModel.processEvent(new RegistrationViewEvent.BookConsultationClicked());
    }

    @OnClick(R.id.btn_find_out_more)
    public void onClickFindOutMore() {
        this.registrationViewModel.processEvent(new RegistrationViewEvent.FindOutMoreClicked());
    }

    @Override
    public void submit() {

    }

}