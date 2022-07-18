package com.waveneuro.ui.dashboard.help;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class HelpCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public HelpCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {

        Intent intent = new Intent(this.mContext, HelpActivity.class);
        this.mContext.startActivity(intent);
    }
}

