package com.waveneuro.ui.base.activity

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.viewbinding.ViewBinding

abstract class BaseWebViewActivity<B : ViewBinding> : BaseActivity<B>() {

    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    protected fun setWebView(webView: WebView?) {
        this.webView = webView
        this.webView?.webViewClient = WebViewClient()
        this.webView?.isClickable = true
        this.webView?.webChromeClient = WebChromeClient()
        this.webView?.settings?.domStorageEnabled = true
    }

    protected fun setUrl(url: String?) {
        webView?.loadUrl(url ?: "")
    }

    protected fun goBack() {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onBackPressed()
        }
    }

}