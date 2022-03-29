package com.waveneuro.ui.dashboard.account;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class AccountCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public AccountCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, AccountActivity.class);
        this.mContext.startActivity(intent);
    }
}

