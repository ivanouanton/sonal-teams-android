package com.waveneuro.ui.user.mfa;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;
import com.waveneuro.ui.user.password.password.confirm.SetNewPasswordActivity;

import javax.inject.Inject;

public class MfaCommand extends NavigationCommand {

    public static final String USERNAME = "username";
    public static final String SESSION = "session";

    private final Context mContext;

    @Inject
    public MfaCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    public void navigate(String username, String session) {
        Intent intent = new Intent(this.mContext, MfaActivity.class);
        intent.putExtra(USERNAME, username);
        intent.putExtra(SESSION, session);
        this.mContext.startActivity(intent);
    }
}
