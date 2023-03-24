package com.waveneuro.ui.dashboard.organization.adapter

import android.content.Context
import android.view.ViewGroup
import com.waveneuro.databinding.ItemProfileOrganizationBinding
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH
import com.waveneuro.ui.model.organization.UiOrganization

class OrganizationAdapter(
    context: Context
) : BaseEntityListAdapter<UiOrganization, OrganizationAdapter.OrganizationVH>(
    context,
    UiOrganization.DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationVH =
        OrganizationVH(
            ItemProfileOrganizationBinding.inflate(inflater, parent, false)
        )

    inner class OrganizationVH(
        private val binding: ItemProfileOrganizationBinding
    ) : EntityVH<UiOrganization>(binding.root) {
        override fun bind(model: UiOrganization) {
            super.bind(model)
            binding.tvOrganization.text = model.name
        }
    }

}