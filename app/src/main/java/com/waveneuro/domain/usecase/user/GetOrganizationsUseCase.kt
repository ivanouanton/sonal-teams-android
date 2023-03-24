package com.waveneuro.domain.usecase.user

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.user.Organization
import com.waveneuro.domain.model.user.UserMapperImpl
import javax.inject.Inject

class GetOrganizationsUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapperImpl
) {

    suspend fun getOrganizations(): List<Organization> =
        userApi.getOrganizations().map(mapper::fromApiToDomain)

}

