package com.waveneuro.domain.model.user

import com.waveneuro.data.api.user.model.user.ApiUserInfoRs

interface UserMapper {
    fun fromApiToDomain(api: ApiUserInfoRs): UserInfo
}