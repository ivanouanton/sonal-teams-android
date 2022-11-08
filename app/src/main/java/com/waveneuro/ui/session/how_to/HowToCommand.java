package com.waveneuro.ui.session.how_to;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class HowToCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public HowToCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {

        Intent intent = new Intent(this.mContext, HowToActivity.class);
        this.mContext.startActivity(intent);
    }
}

