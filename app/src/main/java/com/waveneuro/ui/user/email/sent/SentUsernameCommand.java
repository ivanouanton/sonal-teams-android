package com.waveneuro.ui.user.email.sent;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class SentUsernameCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public SentUsernameCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, SentUsernameActivity.class);
        this.mContext.startActivity(intent);
    }
}