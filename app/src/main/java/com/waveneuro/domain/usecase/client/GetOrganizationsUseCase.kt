package com.waveneuro.domain.usecase.client

import com.asif.abase.domain.base.ObservableUseCase
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.response.organization.OrganizationResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetOrganizationsUseCase @Inject constructor(
    private val dataManager: DataManager
) : ObservableUseCase<List<OrganizationResponse>>() {

    override fun buildUseCaseSingle(): Observable<List<OrganizationResponse>> {
        return dataManager.organizations()
    }

}

