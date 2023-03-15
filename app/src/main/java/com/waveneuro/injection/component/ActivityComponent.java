package com.waveneuro.injection.component;

import android.content.Context;

import com.asif.abase.injection.qualifier.ApplicationContext;
import com.asif.abase.injection.scope.PerActivity;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.injection.module.ActivityModule;
import com.waveneuro.ui.session.complete.SessionCompleteActivity;
import com.waveneuro.ui.session.history.SessionHistoryActivity;
import com.waveneuro.ui.session.session.SessionActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    @ApplicationContext
    Context context();

    DataManager dataManager();

    PreferenceManager preferenceManager();

    AnalyticsManager analyticsManager();


    void inject(SessionActivity sessionActivity);

    void inject(SessionCompleteActivity sessionCompleteActivity);

    void inject(SessionHistoryActivity sessionHistoryActivity);

}
