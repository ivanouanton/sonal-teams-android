package com.waveneuro.ui.session.complete;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class SessionCompleteCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public SessionCompleteCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, SessionCompleteActivity.class);
        this.mContext.startActivity(intent);
    }
}

