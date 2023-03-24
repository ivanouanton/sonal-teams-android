package com.waveneuro.domain.model.user

import com.waveneuro.data.api.user.model.user.ApiUserInfoRs
import com.waveneuro.data.api.user.model.user.ApiOrganizationRs
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

    override fun fromApiToDomain(api: ApiOrganizationRs): Organization = with(api) {
        Organization(
            id, name
        )
    }

}