package com.waveneuro.ui.user.email.forgot;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class ForgotUsernameCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public ForgotUsernameCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, ForgotUsernameActivity.class);
        this.mContext.startActivity(intent);
    }
}
