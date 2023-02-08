package com.waveneuro.ui.dashboard.organization

import android.os.Bundle
import androidx.lifecycle.Observer
import com.asif.abase.data.model.BaseModel
import com.waveneuro.R
import com.waveneuro.data.model.response.user.UserInfoResponse
import com.waveneuro.databinding.ActivityOrganizationBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.organization.OrganizationViewEffect.BackRedirect
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent.BackClicked
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent.Start
import com.waveneuro.ui.dashboard.organization.OrganizationViewState.*
import com.waveneuro.ui.dashboard.organization.adapter.OrganizationAdapter
import com.waveneuro.utils.ext.addItemDecorationWithoutLastDivider
import javax.inject.Inject

class OrganizationActivity : BaseActivity() {

    private lateinit var binding: ActivityOrganizationBinding
    private lateinit var adapter: OrganizationAdapter

    @Inject
    lateinit var viewModel: OrganizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()
        setView()
        setObserver()
        viewModel.processEvent(Start)
    }

    override fun onSuccess(model: BaseModel) {
        super.onSuccess(model)
        if (model is UserInfoResponse) {
            adapter.submitList(viewModel.getOrganizations(model))
        }
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
        viewModel.data.observe(this, Observer { viewState ->
            when (viewState) {
                is Success -> onSuccess(viewState.item)
                is Failure -> onFailure(viewState.error)
                is Loading -> {
                    if (viewState.loading) displayWait("Loading...", null)
                    else removeWait()
                }
                else -> {}
            }

        })
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when(viewEffect) {
                is BackRedirect -> goBack()
            }
        })
    }

    private fun goBack() {
        onBackPressed()
    }

}