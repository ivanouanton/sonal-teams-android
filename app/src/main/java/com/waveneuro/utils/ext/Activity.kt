package com.waveneuro.utils.ext

import android.app.Activity
import com.waveneuro.WaveNeuroApplication
import com.waveneuro.injection.component.ApplicationComponent

fun Activity.getAppComponent(): ApplicationComponent =
    (this.application as WaveNeuroApplication).appComponent