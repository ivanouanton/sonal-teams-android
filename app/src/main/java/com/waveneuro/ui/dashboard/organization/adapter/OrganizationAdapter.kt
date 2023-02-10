package com.waveneuro.ui.dashboard.organization.adapter

import android.content.Context
import android.view.ViewGroup
import com.waveneuro.databinding.ItemProfileOrganizationBinding
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH
import com.waveneuro.ui.model.organization.OrganizationUi

class OrganizationAdapter(
    context: Context
) : BaseEntityListAdapter<OrganizationUi, OrganizationAdapter.OrganizationVH>(
    context,
    OrganizationUi.DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationVH =
        OrganizationVH(
            ItemProfileOrganizationBinding.inflate(inflater, parent, false)
        )

    inner class OrganizationVH(
        private val binding: ItemProfileOrganizationBinding
    ) : EntityVH<OrganizationUi>(binding.root) {
        override fun bind(model: OrganizationUi) {
            super.bind(model)
            binding.tvOrganization.text = model.name
        }
    }

}