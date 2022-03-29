package com.waveneuro.ui.user.password.change;

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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFormActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends BaseFormActivity {

    @NotEmpty(message = "Enter password")
    @Password(message = "Password must at least 8 characters", min = 8,
            scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    @BindView(R.id.et_new_password)
    TextInputLayout etNewPassword;
    @NotEmpty(message = "Enter password again for confirmation")
    @ConfirmPassword
    @BindView(R.id.et_confirm_password)
    TextInputLayout etConfirmPassword;

    @BindView(R.id.tv_check_spam_folder)
    MaterialTextView tvCheckSpamFolder;

    @Inject
    ChangePasswordViewModel changePasswordViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
        contactUsSpanText();
    }

    private void contactUsSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.check_your_spam_folder_or_contact_us));

        spannableString.setSpan(new UnderlineSpan(), 28, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                changePasswordViewModel.processEvent(new ChangePasswordViewEvent.ContactUsClicked());
            }
        }, 28, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 28, 39, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvCheckSpamFolder.setText(spannableString);
        tvCheckSpamFolder.setClickable(true);
        tvCheckSpamFolder.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setObserver() {
        this.changePasswordViewModel.getData().observe(this, changePasswordViewStateObserver);
        this.changePasswordViewModel.getViewEffect().observe(this, changePasswordViewEffectObserver);
    }

    Observer<ChangePasswordViewState> changePasswordViewStateObserver = viewState -> {

    };

    Observer<ChangePasswordViewEffect> changePasswordViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof ChangePasswordViewEffect.Back) {
            goBack();
        } else if (viewEffect instanceof ChangePasswordViewEffect.ResetSuccess) {
            //TODO Need to decide for state or effect
            launchResetSuccessScreen();
        } else if (viewEffect instanceof ChangePasswordViewEffect.ContactUs) {
            launchContactUsScreen();
        }
    };

    private void goBack() {
        //TODO Send to login screen
    }

    private void launchResetSuccessScreen() {

    }

    private void launchContactUsScreen() {

    }


    @Override
    public void submit() {

    }
}