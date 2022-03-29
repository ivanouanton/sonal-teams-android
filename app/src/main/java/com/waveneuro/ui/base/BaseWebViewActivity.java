package com.waveneuro.ui.base;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class BaseWebViewActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setWebView(WebView webView) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.setClickable(true);
        this.webView.setWebChromeClient(new WebChromeClient());
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setSaveFormData(true);
        this.webView.getSettings().setAllowContentAccess(true);
    }

    protected void setUrl(String url) {
        this.webView.loadUrl(url);
    }

    protected void goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            onBackPressed();
        }
    }
}
