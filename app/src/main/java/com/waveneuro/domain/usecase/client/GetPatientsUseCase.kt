package com.waveneuro.domain.usecase.client

import javax.inject.Inject
import com.waveneuro.data.DataManager
import com.asif.abase.domain.base.ObservableUseCase
import com.waveneuro.data.model.response.client.ClientListResponse
import com.asif.abase.domain.base.UseCaseCallback
import io.reactivex.rxjava3.core.Observable

class GetPatientsUseCase @Inject constructor(
    private val dataManager: DataManager
) : ObservableUseCase<ClientListResponse>() {

    private var page: Int? = null
    private var ids: Array<Int>? = null
    private var query: String? = null

    override fun buildUseCaseSingle(): Observable<ClientListResponse> {
        return dataManager.patients(page, query, ids)
    }

    fun execute(
        newPage: Int, newQuery: String, newIds: Array<Int>,
        useCaseCallback: UseCaseCallback<ClientListResponse>
    ) {
        page = newPage
        query = newQuery
        ids = newIds

        super.execute(useCaseCallback)
    }
}