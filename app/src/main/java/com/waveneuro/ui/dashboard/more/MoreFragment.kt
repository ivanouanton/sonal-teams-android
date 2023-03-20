package com.waveneuro.ui.dashboard.more

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.waveneuro.BuildConfig
import com.waveneuro.databinding.FragmentMoreBinding
import com.waveneuro.ui.base.fragment.BaseViewModelFragment
import com.waveneuro.ui.dashboard.account.AccountActivity
import com.waveneuro.ui.dashboard.history.HistoryActivity
import com.waveneuro.ui.dashboard.more.MoreViewEffect.*
import com.waveneuro.ui.dashboard.more.MoreViewEvent.*
import com.waveneuro.ui.dashboard.more.viewmodel.MoreViewModel
import com.waveneuro.ui.dashboard.more.viewmodel.MoreViewModelImpl
import com.waveneuro.ui.dashboard.web.WebActivity
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_SUPPORT
import com.waveneuro.ui.user.login.LoginActivity
import com.waveneuro.utils.ext.getAppComponent

class MoreFragment : BaseViewModelFragment<FragmentMoreBinding, MoreViewModel>() {

    override val viewModel: MoreViewModelImpl by viewModels {
        getAppComponent().moreViewModelFactory()
    }

    override fun initBinding(container: ViewGroup?): FragmentMoreBinding =
        FragmentMoreBinding.inflate(layoutInflater)

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setObserver()
        setView()
    }

    private fun setObserver() {
        with(viewModel) {
            viewEffect.observe(this@MoreFragment, Observer { viewEffect ->
                when(viewEffect) {
                    is ProfileInfo -> startActivity(AccountActivity.newIntent(requireContext()))
                    is DeviceHistory -> startActivity(HistoryActivity.newIntent(requireContext()))
                    is Help -> startActivity(WebActivity.newIntent(requireContext(), PAGE_SUPPORT))
                    is Login -> startActivity(LoginActivity.newIntent(requireContext()))
                }
            })
        }
    }

    private fun setView() {
        binding?.let { binding ->
            with(binding) {
                tvAppVersion.text =
                    "App version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
                llProfile.setOnClickListener {
                    viewModel.processEvent(ProfileInfoClicked)
                }
                llDeviceHistory.setOnClickListener {
                    viewModel.processEvent(DeviceHistoryClicked)
                }
                btnLogOut.setOnClickListener {
                    viewModel.processEvent(LogoutClicked)
                }
                llHelp.setOnClickListener {
                    viewModel.processEvent(HelpClicked)
                }
            }
        }
    }

    companion object {
        fun newInstance(): MoreFragment = MoreFragment()
    }

}