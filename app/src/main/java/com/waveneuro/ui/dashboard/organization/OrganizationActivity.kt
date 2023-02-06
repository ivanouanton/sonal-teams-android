package com.waveneuro.ui.dashboard.organization

import android.os.Bundle
import androidx.lifecycle.Observer
import com.waveneuro.databinding.ActivityOrganizationBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.organization.OrganizationViewEffect.BackRedirect
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent.BackClicked
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent.Start
import javax.inject.Inject

class OrganizationActivity : BaseActivity() {

    private lateinit var binding: ActivityOrganizationBinding

    @Inject
    lateinit var viewModel: OrganizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setObserver()
        viewModel.processEvent(Start)
    }

    private fun setView() {
        with(binding) {
            ivBack.setOnClickListener {
                viewModel.processEvent(BackClicked)
            }
        }
    }

    private fun setObserver() {
        viewModel.data.observe(this, Observer {

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