package com.waveneuro.domain.model.user

import com.waveneuro.data.api.user.model.user.ApiOrganizationRs
import com.waveneuro.data.api.user.model.user.ApiUserInfoRs
import javax.inject.Inject

class UserMapperImpl @Inject constructor() : UserMapper {

    override fun fromApiToDomain(api: ApiUserInfoRs): UserInfo = with(api) {
        UserInfo(
            id,
            firstName,
            lastName,
            email,
            role,
            organizations.map(::fromApiToDomain)
        )
    }

    private fun fromApiToDomain(api: ApiOrganizationRs): OrganizationRs = with(api) {
        OrganizationRs(
            id, name
        )
    }

}