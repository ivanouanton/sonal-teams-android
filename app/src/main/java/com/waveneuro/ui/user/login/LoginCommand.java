package com.waveneuro.ui.user.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class LoginCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public LoginCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, LoginActivity.class);
        this.mContext.startActivity(intent);
        ((Activity) this.mContext).finish();
    }
}