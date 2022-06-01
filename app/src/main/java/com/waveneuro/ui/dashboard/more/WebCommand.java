package com.waveneuro.ui.dashboard.more;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntDef;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;
import com.waveneuro.R;
import com.waveneuro.data.Config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

public class WebCommand extends NavigationCommand {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PAGE_FAQ, PAGE_POLICY, PAGE_CONTACT, PAGE_SUPPORT, PAGE_TERMS_CONDITIONS,
            PAGE_SONAL})
    public @interface WEB_PAGE_SCREEN {
    }

    public static final int PAGE_FAQ = 0;
    public static final int PAGE_POLICY = 1;
    public static final int PAGE_CONTACT = 2;
    public static final int PAGE_SUPPORT = 3;
    public static final int PAGE_TERMS_CONDITIONS = 4;
    public static final int PAGE_SONAL = 5;

    public static final String PAGE_TITLE = "page_title";
    public static final String PAGE_URL = "page_url";

    private final Context mContext;

    @Inject
    public WebCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    public void navigate(@WEB_PAGE_SCREEN int page) {
        Intent intent = new Intent(this.mContext, WebActivity.class);
        switch (page) {
            case PAGE_FAQ:
                intent.putExtra(PAGE_TITLE, mContext.getString(R.string.faq));
                intent.putExtra(PAGE_URL, Config.FAQ_URL);
                break;
            case PAGE_POLICY:
                intent.putExtra(PAGE_TITLE, mContext.getString(R.string.privacy_policy));
                intent.putExtra(PAGE_URL, Config.PRIVACY_POLICY_URL);
                break;
            case PAGE_CONTACT:
                intent.putExtra(PAGE_TITLE, mContext.getString(R.string.contact_us));
                intent.putExtra(PAGE_URL, Config.CONTACT_US_URL);
                break;
            case PAGE_SUPPORT:
                intent.putExtra(PAGE_TITLE, mContext.getString(R.string.support));
                intent.putExtra(PAGE_URL, Config.SUPPORT_URL);
                break;
            case PAGE_TERMS_CONDITIONS:
                intent.putExtra(PAGE_TITLE, mContext.getString(R.string.terms_of_use));
                intent.putExtra(PAGE_URL, Config.TERMS_OF_USE_URL);
                break;
            case PAGE_SONAL:
                intent.putExtra(PAGE_TITLE, mContext.getString(R.string.app_name));
                intent.putExtra(PAGE_URL, Config.SONAL_URL);
                break;
        }
        this.mContext.startActivity(intent);
    }
}

