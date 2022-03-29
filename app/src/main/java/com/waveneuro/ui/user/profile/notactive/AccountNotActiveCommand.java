package com.waveneuro.ui.user.profile.notactive;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class AccountNotActiveCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public AccountNotActiveCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, AccountNotActiveActivity.class);
        this.mContext.startActivity(intent);
    }
}