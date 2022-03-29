package com.waveneuro.ui.user.password.changed;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class ResetPasswordInfoCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public ResetPasswordInfoCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, ResetPasswordInfoActivity.class);
        this.mContext.startActivity(intent);
    }
}
