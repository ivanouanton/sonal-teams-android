package com.waveneuro.injection.component;


import com.asif.abase.injection.scope.Persist;
import com.waveneuro.injection.module.ActivityModule;

import dagger.Component;

@Persist
@Component(dependencies = ApplicationComponent.class)
public interface PersistComponent {
    ActivityComponent activityComponent(ActivityModule activityModule);
}