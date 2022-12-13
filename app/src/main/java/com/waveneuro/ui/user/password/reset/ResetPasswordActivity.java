package com.waveneuro.ui.user.password.reset;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.user.email.forgot.ForgotUsernameCommand;
import com.waveneuro.ui.user.login.LoginCommand;
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeCommand;
import com.waveneuro.ui.user.password.password.first.SetPasswordViewCommand;
import com.waveneuro.ui.user.password.recovery.RecoveryInstructionsCommand;
import com.waveneuro.ui.user.registration.RegistrationCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BaseFormActivity {

    @NotEmpty(trim = true, message = "Enter email")
    @BindView(R.id.et_username)
    TextInputLayout etUsername;

    @BindView(R.id.tv_log_in)
    TextView tvLogIn;

    @BindView(R.id.btn_send_reset_link)
    MaterialButton btnSendLink;

    @Inject
    ResetPasswordViewModel resetPasswordViewModel;

    @Inject
    ForgotUsernameCommand forgotUsernameCommand;
    @Inject
    RegistrationCommand registrationCommand;
    @Inject
    RecoveryInstructionsCommand recoveryInstructionsCommand;
    @Inject
    ForgotPasswordCodeCommand forgotPasswordCodeCommand;

    @Inject
    LoginCommand loginCommand;
    @Inject
    SetPasswordViewCommand setPasswordViewCommand;

    @Inject
    WebCommand webCommand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
        logInSpanText();
        setupInputWatcher();
    }

    private void logInSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.log_in));

        spannableString.setSpan(new UnderlineSpan(), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLogIn.setText(spannableString);
    }

    private void setupInputWatcher() {
        etUsername.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSendLink.setEnabled(count != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setObserver() {
        this.resetPasswordViewModel.getData().observe(this, resetPasswordViewStateObserver);
        this.resetPasswordViewModel.getViewEffect().observe(this, resetPasswordViewEffectObserver);
    }

    Observer<ResetPasswordViewState> resetPasswordViewStateObserver = viewState -> {
        if (viewState instanceof ResetPasswordViewState.Success) {
            ResetPasswordViewState.Success success = (ResetPasswordViewState.Success) viewState;
            launchCheckEmailDialog();
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
        } else if (viewEffect instanceof ResetPasswordViewEffect.LoginRedirect) {
            launchLoginScreen();
        }
    };

    private void launchLoginScreen() {
        loginCommand.navigate();
        finish();
    }

    private void launchCheckEmailDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PopUp);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvContent = dialogView.findViewById(R.id.tv_content);
        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
        tvTitle.setText(R.string.check_your_email);
        tvTitle.setTextSize(24);
        tvContent.setText(R.string.recovery_info);
        btnPrimary.setText(R.string.open_email_app);
        builder.setView(dialogView);
        final AlertDialog ad = builder.create();
        btnPrimary.setOnClickListener(v -> {
            ad.dismiss();
            forgotPasswordCodeCommand.navigate(etUsername.getEditText().getText().toString().trim());
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
            }
        });
        ad.show();
    }

    @OnClick(R.id.tv_log_in)
    public void onClickBack() {
        loginCommand.navigate();
    }

    private void goBack() {
        onBackPressed();
    }

    @Override
    public void submit() {
        this.resetPasswordViewModel.processEvent(
                new ResetPasswordViewEvent.ForgotPasswordClicked(
                        etUsername.getEditText().getText().toString().trim()));
    }

    @OnClick(R.id.btn_send_reset_link)
    public void onClickSendResetLink() {
        this.mValidator.validate();
    }

}