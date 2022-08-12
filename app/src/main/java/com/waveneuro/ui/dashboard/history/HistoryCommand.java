package com.waveneuro.ui.dashboard.history;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;
import com.waveneuro.ui.dashboard.help.HelpActivity;

import javax.inject.Inject;

public class HistoryCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public HistoryCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {

        Intent intent = new Intent(this.mContext, HistoryActivity.class);
        this.mContext.startActivity(intent);
    }
}

