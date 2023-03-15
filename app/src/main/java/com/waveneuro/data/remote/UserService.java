package com.waveneuro.data.remote;

import com.waveneuro.data.model.request.client.ClientRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.response.client.ClientListResponse;
import com.waveneuro.data.model.response.client.ClientResponse;
import com.waveneuro.data.model.response.organization.OrganizationResponse;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @POST("forgot_password_confirm")
    Observable<ForgotPasswordConfirmResponse> forgotPasswordConfirm(@Body ForgotPasswordConfirmRequest request);

    @POST("refresh-token")
    Observable<RefreshResponse> refreshToken();

    @POST("forgot_password_confirm")
    Observable<SetPasswordResponse> changePassword(@Body SetPasswordRequest request);

    @GET("patients")
    Observable<ClientListResponse> getClientList(@Query("page") int page, @Query("organization") Integer[] org, @Query("search") String startsWith);

    @GET("patients/{id}")
    Observable<ClientResponse> getClient(@Path("id") int id);

    @PUT("patients/{id}")
    Observable<ClientResponse> updateClient(@Path("id") int id, @Body ClientRequest request);

    @GET("sonal/protocols/{id}")
    Observable<ProtocolResponse> getProtocolForUser(@Path("id") int id);

    @GET("orgs/me")
    Observable<List<OrganizationResponse>> getOrganizations();
}
