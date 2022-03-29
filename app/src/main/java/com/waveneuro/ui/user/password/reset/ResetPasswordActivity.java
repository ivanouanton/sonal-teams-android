package com.waveneuro.ui.user.password.reset;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.user.login.LoginCommand;
import com.waveneuro.ui.user.password.password.first.SetPasswordViewCommand;
import com.waveneuro.ui.user.password.recovery.RecoveryInstructionsCommand;
import com.waveneuro.ui.user.registration.RegistrationCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseFormActivity {

    @BindView(R.id.tv_forgot_username)
    MaterialTextView tvForgotUsername;
    @BindView(R.id.tv_register)
    MaterialTextView tvRegister;
    @BindView(R.id.tv_sign_in)
    MaterialTextView tvLogin;
    @BindView(R.id.tv_about_us)
    MaterialTextView tvAboutUs;

    @NotEmpty(trim = true, message = "Enter username")
    @BindView(R.id.et_username)
    TextInputLayout etUsername;

    @Inject
    ResetPasswordViewModel resetPasswordViewModel;

    @Inject
    ResetPasswordCommand forgotUsernameCommand;
    @Inject
    RegistrationCommand registrationCommand;
    @Inject
    RecoveryInstructionsCommand recoveryInstructionsCommand;

    @Inject
    LoginCommand loginCommand;
    @Inject
    SetPasswordViewCommand setPasswordViewCommand;

    @Inject
    WebCommand webCommand;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
        forgotUsernameSpanText();
        registerSpanText();
        loginSpanText();
//        aboutUsSpanText();
    }

    private void forgotUsernameSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.forgot_username_q));

        spannableString.setSpan(new UnderlineSpan(), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                resetPasswordViewModel.processEvent(new ResetPasswordViewEvent.ForgotUsernameClicked());
            }
        }, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvForgotUsername.setText(spannableString);
        tvForgotUsername.setClickable(true);
        tvForgotUsername.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void loginSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.sign_in));

        spannableString.setSpan(new UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                resetPasswordViewModel.processEvent(new ResetPasswordViewEvent.LoginClicked());
            }
        }, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLogin.setText(spannableString);
        tvLogin.setClickable(true);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void registerSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.sign_up));

        spannableString.setSpan(new UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                resetPasswordViewModel.processEvent(new ResetPasswordViewEvent.RegisterClicked());
            }
        }, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvRegister.setText(spannableString);
        tvRegister.setClickable(true);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void aboutUsSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.about_us));

        spannableString.setSpan(new UnderlineSpan(), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                resetPasswordViewModel.processEvent(new ResetPasswordViewEvent.AboutUsClicked());
            }
        }, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAboutUs.setText(spannableString);
        tvAboutUs.setClickable(true);
        tvAboutUs.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setObserver() {
        this.resetPasswordViewModel.getData().observe(this, resetPasswordViewStateObserver);
        this.resetPasswordViewModel.getViewEffect().observe(this, resetPasswordViewEffectObserver);
    }

    Observer<ResetPasswordViewState> resetPasswordViewStateObserver = viewState -> {
        if (viewState instanceof ResetPasswordViewState.Success) {
            ResetPasswordViewState.Success success = (ResetPasswordViewState.Success) viewState;
//            onSuccess();
//            launchRecoveryInsScreen();
            launchSetPasswordScreen();
        } else if (viewState instanceof ResetPasswordViewState.Failure) {
            ResetPasswordViewState.Failure error = (ResetPasswordViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof ResetPasswordViewState.Loading) {
            ResetPasswordViewState.Loading loading = ((ResetPasswordViewState.Loading) viewState);
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        }
    };

    Observer<ResetPasswordViewEffect> resetPasswordViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof ResetPasswordViewEffect.BackRedirect) {
            goBack();
        } else if (viewEffect instanceof ResetPasswordViewEffect.ForgotUsernameRedirect) {
            launchForgotUsernameScreen();
        } else if (viewEffect instanceof ResetPasswordViewEffect.RegisterRedirect) {
            launchRegisterScreen();
        } else if (viewEffect instanceof ResetPasswordViewEffect.AboutUsRedirect) {
            launchAboutUsScreen();
        } else if (viewEffect instanceof ResetPasswordViewEffect.LoginRedirect) {
            launchLoginScreen();
        }
    };

    private void launchForgotUsernameScreen() {
//        forgotUsernameCommand.navigate();
        webCommand.navigate(WebCommand.PAGE_SUPPORT);
    }

    private void launchRegisterScreen() {
        registrationCommand.navigate();
    }

    private void launchAboutUsScreen() {

    }

    private void launchLoginScreen() {
        loginCommand.navigate();
        finish();
    }

    private void launchRecoveryInsScreen() {
        recoveryInstructionsCommand.navigate();
    }

    private void launchSetPasswordScreen() {
        setPasswordViewCommand.navigate(etUsername.getEditText().getText().toString());
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        resetPasswordViewModel.processEvent(
                new ResetPasswordViewEvent.BackClicked());
    }

    private void goBack() {
        onBackPressed();
    }


    @Override
    public void submit() {
        this.resetPasswordViewModel.processEvent(
                new ResetPasswordViewEvent.ForgotPasswordClicked(
                        etUsername.getEditText().getText().toString()));
    }

    @OnClick(R.id.btn_send_reset_link)
    public void onClickSendResetLink() {
        this.mValidator.validate();
    }
}