package com.waveneuro.utils.ext

import androidx.fragment.app.Fragment
import com.waveneuro.WaveNeuroApplication
import com.waveneuro.injection.component.ApplicationComponent

fun Fragment.getAppComponent(): ApplicationComponent =
    (requireActivity().application as WaveNeuroApplication).appComponent