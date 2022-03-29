package com.waveneuro.injection.module;

import android.app.Activity;
import android.content.Context;


import com.asif.abase.injection.qualifier.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity){
        this.mActivity=activity;
    }

    @Provides
    public Activity provideActivity(){
        return this.mActivity;
    }

    @Provides
    @ActivityContext
    public Context provideContext(){
        return this.mActivity;
    }
}
