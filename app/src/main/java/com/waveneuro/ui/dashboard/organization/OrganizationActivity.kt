package com.waveneuro.ui.dashboard.organization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.waveneuro.R
import com.waveneuro.databinding.ActivityOrganizationBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.organization.OrganizationViewEffect.BackRedirect
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent.BackClicked
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent.Start
import com.waveneuro.ui.dashboard.organization.adapter.OrganizationAdapter
import com.waveneuro.ui.dashboard.organization.viewmodel.OrganizationViewModel
import com.waveneuro.ui.dashboard.organization.viewmodel.OrganizationViewModelImpl
import com.waveneuro.utils.ext.addItemDecorationWithoutLastDivider
import com.waveneuro.utils.ext.getAppComponent

class OrganizationActivity :
    BaseViewModelActivity<ActivityOrganizationBinding, OrganizationViewModel>() {

    private lateinit var adapter: OrganizationAdapter

    override val viewModel: OrganizationViewModelImpl by viewModels {
        getAppComponent().organizationViewModelFactory()
    }

    override fun initBinding(): ActivityOrganizationBinding =
        ActivityOrganizationBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAdapter()
        setView()
        setObserver()
        viewModel.processEvent(Start)
    }

    private fun setAdapter() {
        adapter = OrganizationAdapter(this)
        binding.rvOrganizations.adapter = adapter

        binding.rvOrganizations.addItemDecorationWithoutLastDivider(resources.getDrawable(R.drawable.divider))
        binding.rvOrganizations.clipToOutline = true
    }

    private fun setView() {
        with(binding) {
            ivBack.setOnClickListener {
                viewModel.processEvent(BackClicked)
            }
        }
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when(viewEffect) {
                is BackRedirect -> goBack()
                is OrganizationViewEffect.Success -> {
                    adapter.submitList(viewEffect.organizations)
                }
            }
        })
    }

    private fun goBack() {
        onBackPressed()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, OrganizationActivity::class.java)
    }

}