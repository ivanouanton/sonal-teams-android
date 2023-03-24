package com.waveneuro.data.api.session

import com.waveneuro.data.api.session.model.session.ApiSessionRq
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionApi {

    @POST("sonal/sessions/user_closure")
    suspend fun addSession(@Body request: ApiSessionRq)

}