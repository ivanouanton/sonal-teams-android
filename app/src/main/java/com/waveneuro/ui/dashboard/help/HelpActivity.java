package com.waveneuro.ui.dashboard.help;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.more.WebCommand;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity {

    @Inject
    WebCommand webCommand;

    @OnClick(R.id.llFaq)
    public void onClickFaq() {
        webCommand.navigate(WebCommand.PAGE_FAQ);
    }

    @OnClick(R.id.llPrivacyPolicy)
    public void onClickPrivacyPolicy() {
        webCommand.navigate(WebCommand.PAGE_POLICY);
    }

    @OnClick(R.id.llTerms)
    public void onClickTerms() {
        webCommand.navigate(WebCommand.PAGE_TERMS_CONDITIONS);
    }

    @OnClick(R.id.btnContact)
    public void onClickContact() {
        webCommand.navigate(WebCommand.PAGE_CONTACT);
    }

    @OnClick(R.id.iv_back)
    public void OnClickBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
    }
}
