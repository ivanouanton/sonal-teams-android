package com.waveneuro.data.remote;


import com.waveneuro.data.model.response.protocol.ProtocolResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface TreatmentService {

    @GET("latest_nest_protocol")
    Observable<ProtocolResponse> protocol();

}
