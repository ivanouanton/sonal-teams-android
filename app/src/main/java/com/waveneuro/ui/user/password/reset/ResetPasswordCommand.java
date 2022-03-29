package com.waveneuro.ui.user.password.reset;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class ResetPasswordCommand extends NavigationCommand {

    private final Context mContext;

    @Inject
    public ResetPasswordCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(this.mContext, ResetPasswordActivity.class);
//        Intent intent = new Intent(this.mContext, ResetPasswordInfoActivity.class);
//        Intent intent = new Intent(this.mContext, ChangePasswordActivity.class);
//        Intent intent = new Intent(this.mContext, RecoveryInstructionsActivity.class);
        this.mContext.startActivity(intent);
    }
}