package com.waveneuro.ui.user.email.forgot;

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
import com.waveneuro.ui.user.password.reset.ResetPasswordCommand;
import com.waveneuro.ui.user.registration.RegistrationCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotUsernameActivity extends BaseFormActivity {

    @BindView(R.id.tv_forgot_password)
    MaterialTextView tvForgotPassword;
    @BindView(R.id.tv_register)
    MaterialTextView tvRegister;
    @BindView(R.id.tv_about_us)
    MaterialTextView tvAboutUs;
    @NotEmpty(trim = true, message = "Enter username")
    @Email
    @BindView(R.id.et_email)
    TextInputLayout etEmail;

    @Inject
    ForgotUsernameViewModel forgotUsernameViewModel;

    @Inject
    ResetPasswordCommand resetPasswordCommand;
    @Inject
    RegistrationCommand registrationCommand;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_forgot_username);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
        forgotPasswordSpanText();
        registerSpanText();
        aboutUsSpanText();
    }

    private void forgotPasswordSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.forgot_password_q));

        spannableString.setSpan(new UnderlineSpan(), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                forgotUsernameViewModel.processEvent(new ForgotUsernameViewEvent.ForgotPasswordClicked());
            }
        }, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvForgotPassword.setText(spannableString);
        tvForgotPassword.setClickable(true);
        tvForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void registerSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.register));

        spannableString.setSpan(new UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                forgotUsernameViewModel.processEvent(new ForgotUsernameViewEvent.RegisterClicked());
            }
        }, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
                forgotUsernameViewModel.processEvent(new ForgotUsernameViewEvent.AboutUsClicked());
            }
        }, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAboutUs.setText(spannableString);
        tvAboutUs.setClickable(true);
        tvAboutUs.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setObserver() {
        this.forgotUsernameViewModel.getData().observe(this, forgotUsernameViewStateObserver);
        this.forgotUsernameViewModel.getViewEffect().observe(this, forgotUsernameViewEffectObserver);
    }

    Observer<ForgotUsernameViewState> forgotUsernameViewStateObserver = viewState -> {
        if (viewState instanceof ForgotUsernameViewState.Success) {
            ForgotUsernameViewState.Success success = (ForgotUsernameViewState.Success) viewState;
            onSuccess(success.getItem());
        } else if (viewState instanceof ForgotUsernameViewState.Failure) {
            ForgotUsernameViewState.Failure error = (ForgotUsernameViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof ForgotUsernameViewState.Loading) {
            ForgotUsernameViewState.Loading loading = ((ForgotUsernameViewState.Loading) viewState);
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        }
    };

    Observer<ForgotUsernameViewEffect> forgotUsernameViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof ForgotUsernameViewEffect.BackRedirect) {
            goBack();
        } else if (viewEffect instanceof ForgotUsernameViewEffect.ForgotPasswordRedirect) {
            launchForgotPasswordScreen();
        } else if (viewEffect instanceof ForgotUsernameViewEffect.RegisterRedirect) {
            launchRegisterScreen();
        } else if (viewEffect instanceof ForgotUsernameViewEffect.AboutUsRedirect) {
            launchAboutUsScreen();
        }
    };

    private void launchForgotPasswordScreen() {
        resetPasswordCommand.navigate();
    }

    private void launchRegisterScreen() {
        registrationCommand.navigate();
    }

    private void launchAboutUsScreen() {

    }

    @Override
    public void submit() {
        this.forgotUsernameViewModel.processEvent(
                new ForgotUsernameViewEvent.ForgotUsernameClicked(
                        etEmail.getEditText().getText().toString()));
    }

    @OnClick(R.id.btn_send_link)
    public void onClickSendLink() {
       this.mValidator.validate();
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        forgotUsernameViewModel.processEvent(
                new ForgotUsernameViewEvent.BackClicked());
    }

    private void goBack() {
        onBackPressed();
    }
}