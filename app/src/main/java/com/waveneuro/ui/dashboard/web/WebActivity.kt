package com.waveneuro.ui.dashboard.web

import android.os.Bundle
import com.waveneuro.R
import com.waveneuro.databinding.ActivityWebBinding
import com.waveneuro.ui.base.BaseWebViewActivity

class WebActivity : BaseWebViewActivity() {

    private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}