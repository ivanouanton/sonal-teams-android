package com.waveneuro.ui.dashboard.organization.adapter

import android.content.Context
import android.view.ViewGroup
import com.waveneuro.databinding.ItemProfileOrganizationBinding
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH
import com.waveneuro.ui.dashboard.organization.adapter.model.OrganizationItem

class OrganizationAdapter(
    context: Context
) : BaseEntityListAdapter<OrganizationItem, OrganizationAdapter.OrganizationVH>(
    context,
    OrganizationItem.DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationVH =
        OrganizationVH(
            ItemProfileOrganizationBinding.inflate(inflater, parent, false)
        )

    inner class OrganizationVH(
        private val binding: ItemProfileOrganizationBinding
    ) : EntityVH<OrganizationItem>(binding.root) {
        override fun bind(model: OrganizationItem) {
            super.bind(model)
            binding.tvOrganization.text = model.name
        }
    }

}