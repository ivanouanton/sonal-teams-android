package com.waveneuro.ui.user.registration

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.waveneuro.data.Config
import com.waveneuro.databinding.ActivityRegistrationBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.user.registration.RegistrationViewEffect.BookConsultation
import com.waveneuro.ui.user.registration.RegistrationViewEffect.FindOutMore
import com.waveneuro.ui.user.registration.RegistrationViewEvent.BookConsultationClicked
import com.waveneuro.ui.user.registration.RegistrationViewEvent.FindOutMoreClicked
import com.waveneuro.ui.user.registration.viewmodel.RegistrationViewModel
import com.waveneuro.ui.user.registration.viewmodel.RegistrationViewModelImpl
import com.waveneuro.utils.ext.getAppComponent

class RegistrationActivity : BaseViewModelActivity<ActivityRegistrationBinding, RegistrationViewModel>() {

    override val viewModel: RegistrationViewModelImpl by viewModels {
        getAppComponent().registrationViewModelFactory()
    }

    override fun initBinding(): ActivityRegistrationBinding =
        ActivityRegistrationBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setObserver()
    }

    private fun setView() {
        with(binding) {
            ivBack.setOnClickListener {
                viewModel.processEvent(RegistrationViewEvent.BackClicked)
            }
            btnBookConsultation.setOnClickListener {
                viewModel.processEvent(BookConsultationClicked)
            }
            btnFindOutMore.setOnClickListener {
                viewModel.processEvent(FindOutMoreClicked)
            }
        }
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is RegistrationViewEffect.Back -> {
                    goBack()
                }
                is BookConsultation -> {
                    launchBookConsultationScreen()
                }
                is FindOutMore -> {
                    launchFindOutMoreScreen()
                }
            }
        })
    }

    private fun goBack() {
        onBackPressed()
        finish()
    }

    private fun launchBookConsultationScreen() {
        val launchBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(Config.BOOK_CONSULTATION_URL))
        startActivity(launchBrowser)
    }

    private fun launchFindOutMoreScreen() {
        val launchBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(Config.FIND_OUT_MORE_URL))
        startActivity(launchBrowser)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, RegistrationActivity::class.java)
    }

}