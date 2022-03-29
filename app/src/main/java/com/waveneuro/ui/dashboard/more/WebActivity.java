package com.waveneuro.ui.dashboard.more;

import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseWebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebActivity extends BaseWebViewActivity {

    @BindView(R.id.tv_title)
    MaterialTextView tvTitle;

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        setWebView(webView);
        if (getIntent().hasExtra(WebCommand.PAGE_TITLE)) {
            setPageTitle(getIntent().getStringExtra(WebCommand.PAGE_TITLE));
        } else {
            setPageTitle(getString(R.string.app_name));
        }
        if (getIntent().hasExtra(WebCommand.PAGE_URL)) {
            setUrl(getIntent().getStringExtra(WebCommand.PAGE_URL));
        } else {
            setUrl("https://www.waveneuro.com");
        }

    }

    protected void setPageTitle(String title) {
        tvTitle.setText(title);
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        goBack();
    }

}