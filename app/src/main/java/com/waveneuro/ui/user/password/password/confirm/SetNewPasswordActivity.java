package com.waveneuro.ui.user.password.password.confirm;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;
import com.waveneuro.ui.user.login.LoginCommand;

import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetNewPasswordActivity extends BaseFormActivity {

    @BindView(R.id.et_password)
    TextInputLayout etPassword;

    @BindView(R.id.tip_password)
    TextInputEditText tipPassword;

    @BindView(R.id.btn_reset_password)
    MaterialButton btnResetPassword;

    @BindView(R.id.tv_condition1)
    TextView tvCondition1;

    @BindView(R.id.tv_condition2)
    TextView tvCondition2;

    @BindView(R.id.tv_condition3)
    TextView tvCondition3;

    @BindView(R.id.tv_condition4)
    TextView tvCondition4;

    @BindView(R.id.tv_log_in)
    TextView tvLogin;

    @Inject
    LoginCommand loginCommand;

    @Inject
    DashboardCommand dashboardCommand;

    @Inject
    SetNewPasswordViewModel setNewPasswordViewModel;

    private String email;
    private String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_set_new_password);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(SetNewPasswordCommand.EMAIL)
                && getIntent().hasExtra(SetNewPasswordCommand.CODE)) {
            email = getIntent().getStringExtra(SetNewPasswordCommand.EMAIL);
            code = getIntent().getStringExtra(SetNewPasswordCommand.CODE);
        }
        setView();
        setObserver();

        tipPassword.addTextChangedListener(mTextEditorWatcher);

        this.setNewPasswordViewModel.processEvent(new SetNewPasswordViewEvent.Start());
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean isLong = s.length()>=8;
            boolean lowerAndUpper = Pattern.compile("[A-Z]").matcher(s).find() && Pattern.compile("[a-z]").matcher(s).find();
            boolean hasNumbers =  Pattern.compile("[0-9]").matcher(s).find();
            boolean hasSpecials =  Pattern.compile("[^a-zA-Z0-9]").matcher(s).find();

            tvCondition1.setTextColor(getResources().getColor(isLong?R.color.aqua:R.color.red));
            tvCondition2.setTextColor(getResources().getColor(lowerAndUpper?R.color.aqua:R.color.red));
            tvCondition3.setTextColor(getResources().getColor(hasNumbers?R.color.aqua:R.color.red));
            tvCondition4.setTextColor(getResources().getColor(hasSpecials?R.color.aqua:R.color.red));

            boolean isCorrect = isLong && lowerAndUpper && hasNumbers && hasSpecials;

            btnResetPassword.setClickable(isCorrect);
            btnResetPassword.setBackgroundColor(getResources().getColor(isCorrect?R.color.aqua:R.color.gray));

        }

        public void afterTextChanged(Editable s) {
            int x = 0;
        }
    };

    private void setView() {
        logInSpanText();
    }

    private void logInSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.log_in));
        spannableString.setSpan(new UnderlineSpan(), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLogin.setText(spannableString);
    }

    private void setObserver() {
        this.setNewPasswordViewModel.getData().observe(this, setNewPasswordViewStateObserver);
        this.setNewPasswordViewModel.getViewEffect().observe(this, setNewPasswordViewEffectObserver);
    }

    Observer<SetNewPasswordViewState> setNewPasswordViewStateObserver = viewState -> {
        if (viewState instanceof SetNewPasswordViewState.Success) {
            SetNewPasswordViewState.Success success = (SetNewPasswordViewState.Success) viewState;

            View view = findViewById(R.id.root);
            final Snackbar snackBar = Snackbar.make(view, R.string.password_has_been_changed, Snackbar.LENGTH_LONG);
            snackBar.setDuration(100000000);
            snackBar.setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackBar.dismiss();
                    loginCommand.navigate();
                }
            });
            snackBar.show();
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
        if (viewEffect instanceof SetNewPasswordViewEffect.Home) { }
    };

    @OnClick(R.id.btn_reset_password)
    public void onClickSubmitNewPassword() {
        submit();
    }

    @OnClick(R.id.tv_log_in)
    public void onClickLogIn() {
        loginCommand.navigate();
    }

    @Override
    public void submit() {
        this.setNewPasswordViewModel.processEvent(
                new SetNewPasswordViewEvent.SetNewPassword(
                        email,
                        code,
                        tipPassword.getText().toString()
                ));
    }
}