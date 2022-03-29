package com.waveneuro.injection.component;

import android.content.Context;

import com.asif.abase.injection.qualifier.ApplicationContext;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    DataManager dataManager();

    PreferenceManager preferenceManager();

    AnalyticsManager analyticsManager();
}
