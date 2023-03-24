package com.waveneuro.domain.usecase.session

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.session.SessionListRs
import com.waveneuro.domain.model.session.SessionMapperImpl
import javax.inject.Inject

class GetSessionHistoryUseCase @Inject constructor(
    private val userApi: UserApi,
    private val mapper: SessionMapperImpl
) {

    suspend fun getSessions(id: Int): SessionListRs =
        mapper.fromApiToDomain(userApi.getSessions(id))

}