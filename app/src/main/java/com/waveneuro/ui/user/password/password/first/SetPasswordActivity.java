package com.waveneuro.ui.user.password.password.first;

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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.user.login.LoginCommand;
import com.waveneuro.ui.user.password.password.confirm.SetNewPasswordCommand;
import com.waveneuro.ui.user.registration.RegistrationCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseFormActivity {

    @BindView(R.id.tv_register)
    MaterialTextView tvRegister;
    @BindView(R.id.tv_sign_in)
    MaterialTextView tvLogin;

    @NotEmpty(trim = true, message = "Enter confirmation code")
    @BindView(R.id.et_confirmation_code)
    TextInputLayout etConfirmationCode;

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
    LoginCommand loginCommand;
    @Inject
    RegistrationCommand registrationCommand;

    @Inject
    SetPasswordViewModel setPasswordViewModel;

    private String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(SetPasswordViewCommand.USERNAME)) {
            userName= getIntent().getStringExtra(SetPasswordViewCommand.USERNAME) ;
        }

        setView();
        setObserver();

        this.setPasswordViewModel.processEvent(new SetPasswordViewEvent.Start());
    }

    private void setView() {
        loginSpanText();
        registerSpanText();
    }

    private void loginSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.sign_in));

        spannableString.setSpan(new UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                setPasswordViewModel.processEvent(new SetPasswordViewEvent.LoginClicked());
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
                setPasswordViewModel.processEvent(new SetPasswordViewEvent.RegisterClicked());
            }
        }, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvRegister.setText(spannableString);
        tvRegister.setClickable(true);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setObserver() {
        this.setPasswordViewModel.getData().observe(this, setPasswordViewStateObserver);
        this.setPasswordViewModel.getViewEffect().observe(this, setPasswordViewEffectObserver);
    }

    Observer<SetPasswordViewState> setPasswordViewStateObserver = viewState -> {
        if (viewState instanceof SetPasswordViewState.Success) {
            SetPasswordViewState.Success success = (SetPasswordViewState.Success) viewState;
            onSuccess();
        } else if (viewState instanceof SetPasswordViewState.Failure) {
            SetPasswordViewState.Failure error = (SetPasswordViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof SetPasswordViewState.Loading) {
            SetPasswordViewState.Loading loading = ((SetPasswordViewState.Loading) viewState);
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        }
    };

    Observer<SetPasswordViewEffect> setPasswordViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof SetPasswordViewEffect.SignInRedirect) {
            launchSignInScreen();
        } else if (viewEffect instanceof SetPasswordViewEffect.SignUpRedirect) {
            launchSignUpScreen();
        }
    };

    private void launchSignInScreen() {
        loginCommand.navigate();
        finish();
    }

    private void launchSignUpScreen() {
        registrationCommand.navigate();
    }

    public void onSuccess() {

    }

    @OnClick(R.id.btn_submit_new_password)
    public void onClickSubmitNewPassword() {
        this.mValidator.validate();
    }

    @Override
    public void submit() {
        this.setPasswordViewModel.processEvent(
                new SetPasswordViewEvent.SetPassword(
                        etConfirmationCode.getEditText().getText().toString(),
                        userName,
                        etNewPassword.getEditText().getText().toString()
                ));
    }
}