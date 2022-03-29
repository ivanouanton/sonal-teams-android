package com.waveneuro.ui.user.password.password.confirm;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class SetNewPasswordCommand extends NavigationCommand {

    public static final String USERNAME = "username";
    public static final String OLD_PASSWORD = "old_password";

    private final Context mContext;

    @Inject
    public SetNewPasswordCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    public void navigate(String username, String password) {
        Intent intent = new Intent(this.mContext, SetNewPasswordActivity.class);
        intent.putExtra(USERNAME,username);
        intent.putExtra(OLD_PASSWORD,password);
        this.mContext.startActivity(intent);
    }
}
