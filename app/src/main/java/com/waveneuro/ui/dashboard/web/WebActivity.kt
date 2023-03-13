package com.waveneuro.ui.dashboard.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.waveneuro.R
import com.waveneuro.data.Config
import com.waveneuro.databinding.ActivityWebBinding
import com.waveneuro.ui.base.activity.BaseWebViewActivity

class WebActivity : BaseWebViewActivity<ActivityWebBinding>() {

    override fun initBinding(): ActivityWebBinding = ActivityWebBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivBack.setOnClickListener { goBack() }
        setView()

        if (intent.hasExtra(WebCommand.PAGE_URL)) {
            setUrl(intent.getStringExtra(WebCommand.PAGE_URL))
        } else {
            setUrl("https://www.waveneuro.com")
        }
    }

    private fun setView() {
        with(binding) {
            setWebView(webView)
            tvTitle.text = if (intent.hasExtra(WebCommand.PAGE_TITLE)) {
                 intent.getStringExtra(WebCommand.PAGE_TITLE)
            } else {
                getString(R.string.app_name)
            }
            ivBack.setOnClickListener { goBack() }
        }
    }

    companion object {
        const val PAGE_FAQ = 0
        const val PAGE_POLICY = 1
        const val PAGE_CONTACT = 2
        const val PAGE_SUPPORT = 3
        const val PAGE_TERMS_CONDITIONS = 4
        const val PAGE_SONAL = 5
        const val PAGE_TITLE = "page_title"
        const val PAGE_URL = "page_url"

        fun newIntent(context: Context, page: Int) = Intent(context, WebActivity::class.java).apply {
            when (page) {
                PAGE_FAQ -> {
                    putExtra(PAGE_TITLE, context.getString(R.string.faq))
                    putExtra(PAGE_URL, Config.FAQ_URL)
                }
                PAGE_POLICY -> {
                    putExtra(PAGE_TITLE, context.getString(R.string.privacy_policy))
                    putExtra(PAGE_URL, Config.PRIVACY_POLICY_URL)
                }
                PAGE_CONTACT -> {
                    putExtra(PAGE_TITLE, context.getString(R.string.contact_us))
                    putExtra(PAGE_URL, Config.CONTACT_US_URL)
                }
                PAGE_SUPPORT -> {
                    putExtra(PAGE_TITLE, context.getString(R.string.support))
                    putExtra(PAGE_URL, Config.SUPPORT_URL)
                }
                PAGE_TERMS_CONDITIONS -> {
                    putExtra(PAGE_TITLE, context.getString(R.string.terms_of_use))
                    putExtra(PAGE_URL, Config.TERMS_OF_USE_URL)
                }
                PAGE_SONAL -> {
                    putExtra(PAGE_TITLE, context.getString(R.string.app_name))
                    putExtra(PAGE_URL, Config.SONAL_URL)
                }
            }
        }
    }

}