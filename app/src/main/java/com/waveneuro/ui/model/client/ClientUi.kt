package com.waveneuro.ui.model.client

import androidx.recyclerview.widget.DiffUtil
import com.waveneuro.domain.model.common.TosStatus
import com.waveneuro.domain.model.client.AlternativeId
import com.waveneuro.domain.model.user.Organization

data class ClientUi(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthday: String?,
    val email: String,
    val username: String?,
    val isMale: Boolean,
    val organization: Organization,
    val isTosSigned: Boolean,
    val tosStatus: TosStatus,
    val alternativeIds: List<AlternativeId>?,
    val imageURLString: String?,
) {

    object DiffCallback : DiffUtil.ItemCallback<ClientUi>() {

        override fun areItemsTheSame(
            oldItem: ClientUi,
            newItem: ClientUi
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ClientUi,
            newItem: ClientUi
        ): Boolean = oldItem == newItem

    }

}
