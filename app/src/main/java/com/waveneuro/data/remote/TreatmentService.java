package com.waveneuro.data.remote;


import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.treatment.TreatmentResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TreatmentService {

    @GET("latest_nest_protocol")
    Observable<ProtocolResponse> protocol();

    @Headers("X-Client: Android")
    @POST("sonal/sessions/user_closure")
    Observable<TreatmentResponse> addTreatment(@Body AddTreatmentRequest request);
}
