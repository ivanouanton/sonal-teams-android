package com.waveneuro.ui.dashboard.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.waveneuro.databinding.ActivityHelpBinding
import com.waveneuro.ui.base.activity.BaseActivity
import com.waveneuro.ui.dashboard.web.WebActivity
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_CONTACT
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_FAQ
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_POLICY
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_TERMS_CONDITIONS

class HelpActivity : BaseActivity<ActivityHelpBinding>() {

    override fun initBinding(): ActivityHelpBinding = ActivityHelpBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView() {
        with(binding) {
            llFaq.setOnClickListener {
                startActivity(WebActivity.newIntent(this@HelpActivity, PAGE_FAQ))
            }
            llPrivacyPolicy.setOnClickListener {
                startActivity(WebActivity.newIntent(this@HelpActivity, PAGE_POLICY))
            }
            llTerms.setOnClickListener {
                startActivity(WebActivity.newIntent(this@HelpActivity, PAGE_TERMS_CONDITIONS))
            }
            btnContact.setOnClickListener {
                startActivity(WebActivity.newIntent(this@HelpActivity, PAGE_CONTACT))
            }
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HelpActivity::class.java)
    }

}