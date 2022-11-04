package com.waveneuro.ui.user.password.code;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputEditText;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.user.email.forgot.ForgotUsernameCommand;
import com.waveneuro.ui.user.login.LoginCommand;
import com.waveneuro.ui.user.login.LoginViewEffect;
import com.waveneuro.ui.user.mfa.MfaCommand;
import com.waveneuro.ui.user.mfa.MfaViewModel;
import com.waveneuro.ui.user.password.password.confirm.SetNewPasswordCommand;
import com.waveneuro.ui.user.password.reset.ResetPasswordCommand;
import com.waveneuro.ui.user.registration.RegistrationCommand;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordCodeActivity extends BaseFormActivity {

    String username = "";
    String session = "";

    ArrayList<TextInputEditText> viewsList = null;

    int currentFocusedPosition = 1;

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
    LoginCommand loginCommand;

    @BindView(R.id.tv_log_in)
    TextView tvLogIn;

    @BindView(R.id.tv_resend_code)
    TextView tvResendCode;

    @BindView(R.id.tv_support)
    TextView tvSupport;


    @Inject
    MfaViewModel mfaViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        setContentView(R.layout.activity_forgot_password_code);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(ForgotPasswordCodeCommand.EMAIL)) {
            username = getIntent().getStringExtra(ForgotPasswordCodeCommand.EMAIL);
        }

        viewsList = new ArrayList<>();
        viewsList.add(findViewById(R.id.tip_code1));
        viewsList.add(findViewById(R.id.tip_code2));
        viewsList.add(findViewById(R.id.tip_code3));
        viewsList.add(findViewById(R.id.tip_code4));
        viewsList.add(findViewById(R.id.tip_code5));
        viewsList.add(findViewById(R.id.tip_code6));

        for (int i = 0; i < viewsList.size(); i++){
            viewsList.get(i).addTextChangedListener(mTextEditorWatcher);
            viewsList.get(i).setOnKeyListener(onKeyListener);
        }

        viewsList.get(0).requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //this.loginViewModel.getViewEffect().observe(this, loginViewEffectObserver);
        this.mfaViewModel.getViewEffect().observe(this, loginViewEffectObserver);

        setView();
    }

    private void setView() {
        logInSpanText();
        supportSpanText();
        resendSpanText();

    }

    private void logInSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.log_in));
        spannableString.setSpan(new UnderlineSpan(), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLogIn.setText(spannableString);
    }

    private void supportSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.support));
        spannableString.setSpan(new UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSupport.setText(spannableString);
    }

    private void resendSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.resend_code));
        spannableString.setSpan(new UnderlineSpan(), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvResendCode.setText(spannableString);
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            int x = 0;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                if (currentFocusedPosition < 6) {
                    viewsList.get(currentFocusedPosition).requestFocus();
                    currentFocusedPosition++;
                } else {
                    setNewPasswordCommand.navigate(username, buildCode());
                }
            }
        }

        public void afterTextChanged(Editable s) {
            int x = 0;
        }
    };

    private String buildCode(){
        StringBuffer mfaCode = new StringBuffer();
        for (int i = 0; i < viewsList.size(); i++){
            mfaCode.append(viewsList.get(i).getText().toString());
        }
        return mfaCode.toString();
    }

    private final View.OnKeyListener onKeyListener  = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if(i == KeyEvent.KEYCODE_DEL) {
                if (currentFocusedPosition > 1) {
                    viewsList.get(currentFocusedPosition - 2).requestFocus();
                    currentFocusedPosition--;
                }
            }
            return false;
        }
    };

    Observer<LoginViewEffect> loginViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof LoginViewEffect.Home) {
            launchHomeScreen();
        } else if (viewEffect instanceof LoginViewEffect.WrongMfaCode) {
            Toast.makeText(this, "Wrong code", Toast.LENGTH_SHORT).show();
        }
    };

    private void launchHomeScreen() {
        dashboardCommand.navigate();
    }

    @Override
    public void submit() {

    }

    @OnClick(R.id.tv_resend_code)
    public void onClickResendCode() {

    }

    @OnClick(R.id.tv_log_in)
    public void onClickLogIn() {
        loginCommand.navigate();

    }

    @OnClick({R.id.tv_support})
    public void onClickSupport() {
        webCommand.navigate(WebCommand.PAGE_SUPPORT);
    }
}