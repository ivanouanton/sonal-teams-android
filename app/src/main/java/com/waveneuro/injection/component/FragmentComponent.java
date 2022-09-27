package com.waveneuro.injection.component;

import android.content.Context;

import com.asif.abase.injection.qualifier.ActivityContext;
import com.asif.abase.injection.scope.PerFragment;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.dashboard.device.DeviceFragment;
import com.waveneuro.ui.dashboard.edit_client.EditClientBottomSheet;
import com.waveneuro.ui.dashboard.home.HomeFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ActivityComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    @ActivityContext
    Context getContext();

    void inject(HomeFragment homeFragment);

    void inject(DeviceFragment deviceFragment);

    void inject(EditClientBottomSheet editClientBottomSheet);
}