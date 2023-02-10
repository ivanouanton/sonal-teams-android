package com.waveneuro.domain.usecase.patient

import javax.inject.Inject
import com.waveneuro.data.DataManager
import com.asif.abase.domain.base.ObservableUseCase
import com.waveneuro.data.model.response.client.ClientListResponse
import com.asif.abase.domain.base.UseCaseCallback
import io.reactivex.rxjava3.core.Observable

class GetPatientsUseCase @Inject constructor(
    private val dataManager: DataManager
) : ObservableUseCase<ClientListResponse>() {

    //TODO check null
    private var page: Int? = null
    private var ids: Array<Int>? = null
    private var query: String? = null

    override fun buildUseCaseSingle(): Observable<ClientListResponse> {
        return dataManager.patients(page, query, ids)
    }

    fun execute(
        newPage: Int?, newQuery: String?, newIds: Array<Int>?,
        useCaseCallback: UseCaseCallback<ClientListResponse>
    ) {
        page = newPage
        query = newQuery
        ids = newIds

        super.execute(useCaseCallback)
    }
}

// TODO
//public class GetPatientsUseCase extends ObservableUseCase<PatientListResponse> {
//
//    private final DataManager dataManager;
//
//    private Integer page;
//    private Integer[] ids;
//    private String startsWith;
//
//    @Inject
//    public GetPatientsUseCase(DataManager dataManager) {
//        this.dataManager = dataManager;
//    }
//
//    @Override
//    public Observable<PatientListResponse> buildUseCaseSingle() {
//        return dataManager.patients(page, startsWith, ids);
//    }
//
//    public void execute(Integer page, String starsWith, Integer[] ids,
//        UseCaseCallback<PatientListResponse> useCaseCallback) {
//        this.page = page;
//        this.ids=ids;
//        this.startsWith = starsWith;
//        super.execute(useCaseCallback);
//    }
//}