package com.waveneuro.ui.dashboard.web

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import com.waveneuro.R
import com.waveneuro.data.Config
import javax.inject.Inject

class WebCommand @Inject constructor(
    @ActivityContext private val context: Context
) : NavigationCommand() {

    fun navigate(page: Int) {
        val intent = Intent(context, WebActivity::class.java)
        when (page) {
            PAGE_FAQ -> {
                intent.putExtra(PAGE_TITLE, context.getString(R.string.faq))
                intent.putExtra(PAGE_URL, Config.FAQ_URL)
            }
            PAGE_POLICY -> {
                intent.putExtra(PAGE_TITLE, context.getString(R.string.privacy_policy))
                intent.putExtra(PAGE_URL, Config.PRIVACY_POLICY_URL)
            }
            PAGE_CONTACT -> {
                intent.putExtra(PAGE_TITLE, context.getString(R.string.contact_us))
                intent.putExtra(PAGE_URL, Config.CONTACT_US_URL)
            }
            PAGE_SUPPORT -> {
                intent.putExtra(PAGE_TITLE, context.getString(R.string.support))
                intent.putExtra(PAGE_URL, Config.SUPPORT_URL)
            }
            PAGE_TERMS_CONDITIONS -> {
                intent.putExtra(PAGE_TITLE, context.getString(R.string.terms_of_use))
                intent.putExtra(PAGE_URL, Config.TERMS_OF_USE_URL)
            }
            PAGE_SONAL -> {
                intent.putExtra(PAGE_TITLE, context.getString(R.string.app_name))
                intent.putExtra(PAGE_URL, Config.SONAL_URL)
            }
        }
        context.startActivity(intent)
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
    }
}