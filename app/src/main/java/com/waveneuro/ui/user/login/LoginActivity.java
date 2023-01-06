package com.waveneuro.ui.user.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;

import androidx.lifecycle.Observer;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.user.email.forgot.ForgotUsernameCommand;
import com.waveneuro.ui.user.mfa.MfaCommand;
import com.waveneuro.ui.user.password.password.confirm.SetNewPasswordCommand;
import com.waveneuro.ui.user.password.reset.ResetPasswordCommand;
import com.waveneuro.ui.user.registration.RegistrationCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginActivity extends BaseFormActivity {

    @NotEmpty(trim = true, message = "Enter username")
    @BindView(R.id.et_username)
    TextInputLayout etUsername;
    @NotEmpty(message = "Enter password")
    @Password(message = "Password must at least 8 characters", min = 8)
    @BindView(R.id.et_password)
    TextInputLayout etPassword;
    @BindView(R.id.chk_remember_me)
    MaterialCheckBox chkRememberMe;
    @BindView(R.id.tv_recover_here)
    MaterialTextView tvRecover;

    @Inject
    DashboardCommand dashboardCommand;
    @Inject
    ForgotUsernameCommand forgotUsernameCommand;
    @Inject
    ResetPasswordCommand resetPasswordCommand;
    @Inject
    RegistrationCommand registrationCommand;
    @Inject
    SetNewPasswordCommand setNewPasswordCommand;
    @Inject
    MfaCommand mfaCommand;
    @Inject
    WebCommand webCommand;

    @Inject
    LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setView();
        setObserver();

        this.loginViewModel.processEvent(new LoginViewEvent.Start());
    }

    private void setView() {
        recoverHereSpan();
    }

    private void recoverHereSpan() {
        SpannableString spannableString = new SpannableString(getString(R.string.forgot_password));

        spannableString.setSpan(new UnderlineSpan(), 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvRecover.setText(spannableString);

        tvRecover.setOnClickListener(view -> loginViewModel.processEvent(new LoginViewEvent.ForgotPasswordClicked()));
    }


    private void setObserver() {
        this.loginViewModel.getData().observe(this, loginViewStateObserver);
        this.loginViewModel.getViewEffect().observe(this, loginViewEffectObserver);
    }

    Observer<LoginViewState> loginViewStateObserver = viewState -> {
        if (viewState instanceof LoginViewState.Success) {
            LoginViewState.Success success = (LoginViewState.Success) viewState;
            onSuccess(success.getItem());
        } else if (viewState instanceof LoginViewState.Failure) {
            LoginViewState.Failure error = (LoginViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof LoginViewState.Loading) {
            LoginViewState.Loading loading = ((LoginViewState.Loading) viewState);
            Timber.e("LOADING :: %s", loading.getLoading());
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        }
    };

    Observer<LoginViewEffect> loginViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof LoginViewEffect.ForgotPassword) {
            launchForgotPasswordScreen();
        } else if (viewEffect instanceof LoginViewEffect.RememberMe) {
            LoginViewEffect.RememberMe rememberMe = (LoginViewEffect.RememberMe) viewEffect;
            setRememberData(rememberMe.getUsername());
        } else if (viewEffect instanceof LoginViewEffect.Register) {
            launchRegistrationScreen();
        } else if (viewEffect instanceof LoginViewEffect.Support) {
        } else if (viewEffect instanceof LoginViewEffect.SetNewPassword) {
            launchSetNewPasswordScreen();
        } else if (viewEffect instanceof LoginViewEffect.EnterMfaCode) {
            LoginViewEffect.EnterMfaCode enterMfaCode = (LoginViewEffect.EnterMfaCode) viewEffect;
            mfaCommand.navigate(etUsername.getEditText().getText().toString().trim(), enterMfaCode.getSession());
        }
    };

    private void setRememberData(String username) {
        etUsername.getEditText().setText(username);
        chkRememberMe.setChecked(true);
    }

    private void launchSetNewPasswordScreen() {
        setNewPasswordCommand.navigate(etUsername.getEditText().getText().toString(),
                etPassword.getEditText().getText().toString());
    }

    private void launchForgotPasswordScreen() {
        resetPasswordCommand.navigate();
    }

    private void launchRegistrationScreen() {
        registrationCommand.navigate();
    }

    @OnClick(R.id.btn_login)
    public void onClickLogin() {
        this.mValidator.validate();
    }

    @Override
    public void submit() {
        if(chkRememberMe.isChecked()) {
            this.loginViewModel.processEvent(new LoginViewEvent.RememberUser(
                    etUsername.getEditText().getText().toString().trim()));
        }else {
            this.loginViewModel.processEvent(new LoginViewEvent.ClearRememberUser());
        }
        this.loginViewModel.processEvent(new LoginViewEvent.LoginClicked(
                etUsername.getEditText().getText().toString().trim(),
                etPassword.getEditText().getText().toString().trim()));
    }

}