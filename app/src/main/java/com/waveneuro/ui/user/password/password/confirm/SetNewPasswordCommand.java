package com.waveneuro.ui.user.password.password.confirm;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class SetNewPasswordCommand extends NavigationCommand {

    public static final String EMAIL = "email";
    public static final String CODE = "code";

    private final Context mContext;

    @Inject
    public SetNewPasswordCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    public void navigate(String email, String code) {
        Intent intent = new Intent(this.mContext, SetNewPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EMAIL, email);
        intent.putExtra(CODE, code);
        this.mContext.startActivity(intent);
    }
}
