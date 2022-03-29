package com.waveneuro.ui.user.password.password.first;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class SetPasswordViewCommand extends NavigationCommand {

    public static final String USERNAME = "username";

    private final Context mContext;

    @Inject
    public SetPasswordViewCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate(String username) {
        Intent intent = new Intent(this.mContext, SetPasswordActivity.class);
        intent.putExtra(USERNAME,username);
        this.mContext.startActivity(intent);
    }
}
