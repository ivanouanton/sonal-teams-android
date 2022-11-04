package com.waveneuro.ui.user.password.code;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class ForgotPasswordCodeCommand extends NavigationCommand {

    public static final String EMAIL = "email";

    private final Context mContext;

    @Inject
    public ForgotPasswordCodeCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    public void navigate(String email) {
        Intent intent = new Intent(this.mContext, ForgotPasswordCodeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EMAIL, email);
        this.mContext.startActivity(intent);
    }
}
