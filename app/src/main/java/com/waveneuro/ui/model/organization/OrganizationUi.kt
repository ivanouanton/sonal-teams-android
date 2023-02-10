package com.waveneuro.ui.model.organization

import androidx.recyclerview.widget.DiffUtil

data class OrganizationUi(
    val id: Int,
    val name: String,
) {

    object DiffCallback : DiffUtil.ItemCallback<OrganizationUi>() {

        override fun areItemsTheSame(
            oldItem: OrganizationUi,
            newItem: OrganizationUi
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: OrganizationUi,
            newItem: OrganizationUi
        ): Boolean = oldItem == newItem

    }

}