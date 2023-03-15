package com.waveneuro.data.api.session

import com.waveneuro.data.api.user.model.session.ApiSessionRq
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionApi {

//    @GET("latest_nest_protocol")
//    fun protocol(): Observable<ProtocolResponse?>?

    @POST("sonal/sessions/user_closure")
    suspend fun addSession(@Body request: ApiSessionRq)

}