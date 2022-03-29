package com.waveneuro.injection.module;

import android.content.Context;

import androidx.fragment.app.Fragment;


import com.asif.abase.injection.qualifier.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private final Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Provides
    public Fragment provideFragment() {
        return this.mFragment;
    }

    @Provides
    @ActivityContext
    public Context provideContext() {
        return this.mFragment.getContext();
    }
}