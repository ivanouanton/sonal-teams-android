package com.waveneuro.ui.session.history;

import android.content.Context;
import android.content.Intent;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.injection.qualifier.ActivityContext;

import javax.inject.Inject;

public class SessionHistoryCommand extends NavigationCommand {

    public static final String USER_ID = "user_id";
    public static final String NAME = "name";

    private final Context mContext;

    @Inject
    public SessionHistoryCommand(@ActivityContext Context mContext) {
        this.mContext = mContext;
    }

    public void navigate(String value, String name) {
        Intent intent = new Intent(this.mContext, SessionHistoryActivity.class);
        intent.putExtra(USER_ID, value);
        intent.putExtra(NAME, name);
        this.mContext.startActivity(intent);
    }
}