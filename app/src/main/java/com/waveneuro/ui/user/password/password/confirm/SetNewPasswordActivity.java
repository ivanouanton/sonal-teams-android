package com.waveneuro.ui.user.password.password.confirm;

import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetNewPasswordActivity extends BaseFormActivity {

    @NotEmpty(message = "Enter password")
    @Password(message = "Password must at least 8 characters", min = 8,
            scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    @BindView(R.id.et_new_password)
    TextInputLayout etNewPassword;

    @NotEmpty(message = "Re-Enter password")
    @Password(message = "Password not matched", min = 8,
            scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    @BindView(R.id.et_confirm_password)
    TextInputLayout etConfirmPassword;

    @Inject
    DashboardCommand dashboardCommand;

    @Inject
    SetNewPasswordViewModel setNewPasswordViewModel;

    private String userName;
    private String oldPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_set_new_password);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(SetNewPasswordCommand.USERNAME)
                && getIntent().hasExtra(SetNewPasswordCommand.OLD_PASSWORD)) {
            userName= getIntent().getStringExtra(SetNewPasswordCommand.USERNAME) ;
            oldPassword= getIntent().getStringExtra(SetNewPasswordCommand.OLD_PASSWORD);
        }
        setView();
        setObserver();

        this.setNewPasswordViewModel.processEvent(new SetNewPasswordViewEvent.Start());
    }

    private void setView() {

    }

    private void setObserver() {
        this.setNewPasswordViewModel.getData().observe(this, setNewPasswordViewStateObserver);
        this.setNewPasswordViewModel.getViewEffect().observe(this, setNewPasswordViewEffectObserver);
    }

    Observer<SetNewPasswordViewState> setNewPasswordViewStateObserver = viewState -> {
        if (viewState instanceof SetNewPasswordViewState.Success) {
            SetNewPasswordViewState.Success success = (SetNewPasswordViewState.Success) viewState;
            onSuccess();
        } else if (viewState instanceof SetNewPasswordViewState.Failure) {
            SetNewPasswordViewState.Failure error = (SetNewPasswordViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof SetNewPasswordViewState.Loading) {
            SetNewPasswordViewState.Loading loading = ((SetNewPasswordViewState.Loading) viewState);
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        }
    };

    Observer<SetNewPasswordViewEffect> setNewPasswordViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof SetNewPasswordViewEffect.Home) {
            launchHomeScreen();
        }
    };

    public void onSuccess() {

    }

    private void launchHomeScreen() {
        dashboardCommand.navigate();
        finish();
    }

    @OnClick(R.id.btn_submit_new_password)
    public void onClickSubmitNewPassword() {
        this.mValidator.validate();
    }

    @Override
    public void submit() {
        this.setNewPasswordViewModel.processEvent(
                new SetNewPasswordViewEvent.SetNewPassword(
                        userName,
                        oldPassword,
                        etNewPassword.getEditText().getText().toString()
                ));
    }
}