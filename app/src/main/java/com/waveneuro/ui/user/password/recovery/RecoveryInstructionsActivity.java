package com.waveneuro.ui.user.password.recovery;

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

import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoveryInstructionsActivity extends BaseActivity {

    @BindView(R.id.tv_check_spam_folder)
    MaterialTextView tvCheckSpamFolder;


    @Inject
    RecoveryInstructionsViewModel recoveryInstructionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_recovery_instructions);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
        contactUsSpanText();
    }

    private void contactUsSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.check_your_spam_folder_or_contact_us));

        spannableString.setSpan(new UnderlineSpan(), 28, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                recoveryInstructionsViewModel.processEvent(new RecoveryInstructionsViewEvent.ContactUsClicked());
            }
        }, 28, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 28, 38, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvCheckSpamFolder.setText(spannableString);
        tvCheckSpamFolder.setClickable(true);
        tvCheckSpamFolder.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setObserver() {
        this.recoveryInstructionsViewModel.getData().observe(this, recoveryInstructionsViewStateObserver);
        this.recoveryInstructionsViewModel.getViewEffect().observe(this, recoveryInstructionsViewEffectObserver);
    }

    Observer<RecoveryInstructionsViewState> recoveryInstructionsViewStateObserver = viewState -> {

    };

    Observer<RecoveryInstructionsViewEffect> recoveryInstructionsViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof RecoveryInstructionsViewEffect.Back) {
            goBack();
        } else if (viewEffect instanceof RecoveryInstructionsViewEffect.ContactUs) {
            launchContactUsScreen();
        } else if (viewEffect instanceof RecoveryInstructionsViewEffect.EmailApp) {
            launchEmailScreen();
        } else if (viewEffect instanceof RecoveryInstructionsViewEffect.Skip) {
        }
    };

    private void goBack() {
        //TODO Send to login screen
    }

    private void launchContactUsScreen() {

    }

    private void launchEmailScreen() {

    }


    @OnClick(R.id.btn_open_email_app)
    public void onClickOpenMailApp() {
        this.recoveryInstructionsViewModel.processEvent(new RecoveryInstructionsViewEvent.OpenEmailAppClicked());
    }

    @OnClick(R.id.tv_skip_later)
    public void onClickSkip() {
        this.recoveryInstructionsViewModel.processEvent(new RecoveryInstructionsViewEvent.SkipClicked());
    }
}