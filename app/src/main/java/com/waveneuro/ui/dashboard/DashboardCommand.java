package com.waveneuro.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class DashboardCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public DashboardCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.mContext.startActivity(intent);
        if (this.mContext instanceof Activity)
            ((Activity) this.mContext).finish();
    }
}
