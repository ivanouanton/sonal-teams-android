package com.waveneuro.ui.user.registration;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class RegistrationCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public RegistrationCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, RegistrationActivity.class);
        this.mContext.startActivity(intent);
    }
}