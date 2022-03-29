package com.waveneuro.ui.user.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

import androidx.lifecycle.Observer;

import com.asif.abase.data.model.BaseModel;
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
    @BindView(R.id.tv_forgot_password)
    MaterialTextView tvForgotPassword;
    @BindView(R.id.tv_register)
    MaterialTextView tvRegister;
    @BindView(R.id.tv_support)
    MaterialTextView tvSupport;
    @BindView(R.id.chk_remember_me)
    MaterialCheckBox chkRememberMe;

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
        loginSpanText();
        registerSpanText();
        supportSpanText();
    }

    private void loginSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.forgot_username_or_password));

        spannableString.setSpan(new UnderlineSpan(), 7, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 19, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                loginViewModel.processEvent(new LoginViewEvent.ForgotUsernameClicked());
            }
        }, 7, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                loginViewModel.processEvent(new LoginViewEvent.ForgotPasswordClicked());
            }
        }, 19, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 7, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 19, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
                loginViewModel.processEvent(new LoginViewEvent.RegisterClicked());
            }
        }, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvRegister.setText(spannableString);
    }

    private void supportSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.need_help_support));

        spannableString.setSpan(new UnderlineSpan(), 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                loginViewModel.processEvent(new LoginViewEvent.SupportClicked());
            }
        }, 11, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 11, 18, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSupport.setText(spannableString);
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
        if (viewEffect instanceof LoginViewEffect.Home) {
            launchHomeScreen();
        } else if (viewEffect instanceof LoginViewEffect.ForgotUsername) {
            launchForgotUsernameScreen();
        } else if (viewEffect instanceof LoginViewEffect.ForgotPassword) {
            launchForgotPasswordScreen();
        } else if (viewEffect instanceof LoginViewEffect.RememberMe) {
            LoginViewEffect.RememberMe rememberMe = (LoginViewEffect.RememberMe) viewEffect;
            setRememberData(rememberMe.getUsername(), rememberMe.getPassword());
        } else if (viewEffect instanceof LoginViewEffect.Register) {
            launchRegistrationScreen();
        } else if (viewEffect instanceof LoginViewEffect.Support) {
        } else if (viewEffect instanceof LoginViewEffect.SetNewPassword) {
            launchSetNewPasswordScreen();
        }
    };

    private void setRememberData(String username, String password) {
        etUsername.getEditText().setText(""+username);
        etPassword.getEditText().setText(""+password);
        chkRememberMe.setChecked(true);
    }

    private void launchSetNewPasswordScreen() {
        setNewPasswordCommand.navigate(etUsername.getEditText().getText().toString(),
                etPassword.getEditText().getText().toString());
    }

    private void launchHomeScreen() {
        dashboardCommand.navigate();
    }

    private void launchForgotUsernameScreen() {
//        forgotUsernameCommand.navigate();
        webCommand.navigate(WebCommand.PAGE_SUPPORT);
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

    @OnClick(R.id.tv_forgot_password)
    public void onClickForgotUsername() {
//        this.loginViewModel.processEvent(new LoginViewEvent.ForgotUsernameClicked());
    }

    public void onClickForgotPassword() {
        this.loginViewModel.processEvent(new LoginViewEvent.ForgotPasswordClicked());
    }

    @OnClick(R.id.tv_register)
    public void onClickRegister() {
        this.loginViewModel.processEvent(new LoginViewEvent.RegisterClicked());
    }

    @OnClick(R.id.tv_support)
    public void onClickSupport() {
        //DONE Make event
//        this.loginViewModel.processEvent(new LoginViewEvent.ForgotPasswordClicked());
        webCommand.navigate(WebCommand.PAGE_SUPPORT);
    }

    @Override
    public void submit() {
        if(chkRememberMe.isChecked())
            this.loginViewModel.processEvent(new LoginViewEvent.RememberUser(
                    etUsername.getEditText().getText().toString().trim(),
                    etPassword.getEditText().getText().toString().trim()));
        else
            this.loginViewModel.processEvent(new LoginViewEvent.ClearRememberUser());
        this.loginViewModel.processEvent(new LoginViewEvent.LoginClicked(
                etUsername.getEditText().getText().toString().trim(),
                etPassword.getEditText().getText().toString().trim()));
    }

    @Override
    public void onSuccess(BaseModel model) {
        super.onSuccess(model);
        launchHomeScreen();
    }
}