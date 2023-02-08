package com.waveneuro.ui.dashboard.organization.adapter.model

import androidx.recyclerview.widget.DiffUtil

data class OrganizationItem(
    val id: Int,
    val name: String,
) {

    object DiffCallback : DiffUtil.ItemCallback<OrganizationItem>() {

        override fun areItemsTheSame(
            oldItem: OrganizationItem,
            newItem: OrganizationItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: OrganizationItem,
            newItem: OrganizationItem
        ): Boolean = oldItem == newItem

    }

}