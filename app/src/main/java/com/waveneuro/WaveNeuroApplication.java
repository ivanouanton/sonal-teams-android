package com.waveneuro;

import android.app.Application;

import com.ap.ble.BluetoothManager;
import com.asif.abase.utils.APDebugTree;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.waveneuro.injection.component.ApplicationComponent;
import com.waveneuro.injection.component.DaggerApplicationComponent;
import com.waveneuro.injection.module.ApplicationModule;

import timber.log.Timber;

public class WaveNeuroApplication extends Application {

    public ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO Connection listener
        initializeLoggers();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        BluetoothManager.getInstance().init(this);
        this.appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initializeLoggers() {
        Timber.plant(new APDebugTree());
    }


}
