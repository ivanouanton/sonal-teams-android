package com.waveneuro.ui.dashboard.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.waveneuro.BuildConfig
import com.waveneuro.databinding.FragmentMoreBinding
import com.waveneuro.injection.component.DaggerFragmentComponent
import com.waveneuro.injection.module.FragmentModule
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.base.BaseFragment
import com.waveneuro.ui.dashboard.account.AccountActivity
import com.waveneuro.ui.dashboard.history.HistoryActivity
import com.waveneuro.ui.dashboard.more.MoreViewEffect.*
import com.waveneuro.ui.dashboard.more.MoreViewEvent.*
import com.waveneuro.ui.dashboard.web.WebActivity
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_SUPPORT
import com.waveneuro.ui.user.login.LoginActivity
import javax.inject.Inject

internal class MoreFragment : BaseFragment() {

    private lateinit var binding: FragmentMoreBinding

    @Inject
    lateinit var moreViewModel: MoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent = DaggerFragmentComponent.builder()
            .activityComponent((activity as BaseActivity?)?.activityComponent())
            .fragmentModule(FragmentModule(this))
            .build()
        fragmentComponent.inject(this)

        setObserver()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater, container, false)
        setView()

        return binding.root
    }

    private fun setObserver() {
        with(moreViewModel) {
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
        with(binding) {
            tvAppVersion.text =
                "App version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            llProfile.setOnClickListener {
                moreViewModel.processEvent(ProfileInfoClicked)
            }
            llDeviceHistory.setOnClickListener {
                moreViewModel.processEvent(DeviceHistoryClicked)
            }
            btnLogOut.setOnClickListener {
                moreViewModel.processEvent(LogoutClicked)
            }
            llHelp.setOnClickListener {
                moreViewModel.processEvent(HelpClicked)
            }
        }
    }

    companion object {
        fun newInstance(): MoreFragment = MoreFragment()
    }
}