package com.waveneuro.ui.introduction.splash

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.waveneuro.databinding.ActivitySplashBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.DashboardActivity
import com.waveneuro.ui.introduction.splash.viewmodel.SplashViewModel
import com.waveneuro.ui.introduction.splash.viewmodel.SplashViewModelImpl
import com.waveneuro.ui.user.login.LoginActivity
import com.waveneuro.utils.ext.getAppComponent

class SplashActivity : BaseViewModelActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModelImpl by viewModels {
        getAppComponent().splashViewModelFactory()
    }

    override fun initBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        setObserver()
        viewModel.processEvent(SplashViewEvent.Start)
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is SplashViewEffect.Login -> launchLoginScreen()
                is SplashViewEffect.Home -> launchHomeScreen()
            }
        })
    }
    private fun launchLoginScreen() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    private fun launchHomeScreen() {
        startActivity(DashboardActivity.newIntent(this))
        finish()
    }

}