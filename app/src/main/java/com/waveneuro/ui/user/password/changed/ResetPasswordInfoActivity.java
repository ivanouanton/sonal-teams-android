package com.waveneuro.ui.user.password.changed;

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

public class ResetPasswordInfoActivity extends BaseActivity {

    @BindView(R.id.tv_label_reset_password_info)
    MaterialTextView tvLabelResetPasswordInfo;

    @Inject
    ResetPasswordInfoViewModel resetPasswordInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_reset_password_info);
        ButterKnife.bind(this);

        setView();
        setObserver();
    }

    private void setView() {
        //TODO set checkmark animation
        resetPasswordInfoSpanText();
    }

    private void resetPasswordInfoSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.reset_password_info));

        spannableString.setSpan(new UnderlineSpan(), 32, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                resetPasswordInfoViewModel.processEvent(new ResetPasswordInfoViewEvent.LoginClicked());
            }
        }, 32, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 32, 43, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLabelResetPasswordInfo.setText(spannableString);
        tvLabelResetPasswordInfo.setClickable(true);
        tvLabelResetPasswordInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setObserver() {
        this.resetPasswordInfoViewModel.getData().observe(this, resetPasswordInfoViewStateObserver);
        this.resetPasswordInfoViewModel.getViewEffect().observe(this, resetPasswordInfoViewEffectObserver);
    }

    Observer<ResetPasswordInfoViewState> resetPasswordInfoViewStateObserver = viewState -> {

    };

    Observer<ResetPasswordInfoViewEffect> resetPasswordInfoViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof ResetPasswordInfoViewEffect.Back) {
            goBack();
        }
    };

    private void goBack() {
        //TODO send back to login
    }
}